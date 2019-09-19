/*
 * Copyright 2019 Pengcheng Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.caipengcheng.ssimpledi4j.container

import cn.caipengcheng.ssimpledi4j.annotation.Component
import cn.caipengcheng.ssimpledi4j.exception.ContainerNotInit
import cn.caipengcheng.ssimpledi4j.exception.PackageNotFoundException
import org.slf4j.LoggerFactory
import java.io.File
import java.net.JarURLConnection
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Cai Pengcheng
 * Create Date: 2019-09-17
 */

/**
 * Class 文件后缀标识。
 */
private const val SUFFIX_CLASS_FILE = ".class"

/**
 * File 的 URL 标识。
 */
private const val URL_FILE = "file"

/**
 * JAR 的 URL 标识。
 */
private const val URL_JAR = "jar"

private var isInit = false

private val logger = LoggerFactory.getLogger("ClassContainer")

/**
 * 存储已加载的 Class 。
 */
private lateinit var classes: List<Class<*>>

/**
 * 初始化 Class 容器。
 */
fun classContainerInit(mainClass: Class<*>) {

    if (isInit) {
        logger.warn("Class container already initialized.")
        return
    }

    val url = Thread.currentThread().contextClassLoader
        .getResource(mainClass.packageName.replace('.', File.separatorChar))
        ?: throw PackageNotFoundException("Can not get the jar classpath.")

    logger.info("Load classes which in '${url.path}'.")

    val clsList = mutableListOf<Class<*>>()

    if (url.protocol == URL_FILE) {
        loadClassFromFile(url.toURI(), mainClass.packageName, clsList, true)
    } else if (url.protocol == URL_JAR) {
        loadClassFromJar(url.toURI(), clsList)
    }

    classes = clsList.filter { it.isAnnotationPresent(Component::class.java) }.toList()

    isInit = true
}

/**
 * 加载 Classes 并存储进[classes]中。
 */
private fun loadClasses(className: String, clsList: MutableList<Class<*>>) {
    logger.info("Load class '${className}'.")
    clsList.add(Class.forName(className))
}

/**
 * 从 Class 文件中加载。
 */
private fun loadClassFromFile(uri: URI, packageName: String, clsList: MutableList<Class<*>>, isBasePackage: Boolean) {
    val file = File(uri)

    val pckName = if (isBasePackage) packageName else "$packageName."
    logger.info(pckName)
    logger.info(uri.path)
    if (file.isDirectory) {
        Files.list(Paths.get(uri))
            .forEach {
                loadClassFromFile(it.toUri(),
                    "$pckName${if (isBasePackage) "" else file.name}", clsList, false)
            }
    } else {
        val className = "$pckName${file.name.substring(0, file.name.lastIndexOf('.'))}"
        loadClasses(className, clsList)
    }
}

/**
 * 从 Jar 文件中加载。
 */
private fun loadClassFromJar(uri: URI, clsList: MutableList<Class<*>>) {
    val jarFile = (uri.toURL().openConnection() as JarURLConnection).jarFile
    val jarEntries = jarFile.entries()
    while (jarEntries.hasMoreElements()) {
        val jarEntry = jarEntries.nextElement()
        val name = jarEntry.name
        if (name.endsWith(SUFFIX_CLASS_FILE)) {
            val className = name.substring(0 until name.lastIndexOf('.'))
                .replace(File.separatorChar, '.')
            loadClasses(className, clsList)
        }
    }
}

fun getLoadedClassesOfBean(): List<Class<*>> {
    if (!isInit) {
        throw ContainerNotInit("The Class container is not initialization.")
    }

    return classes
}

