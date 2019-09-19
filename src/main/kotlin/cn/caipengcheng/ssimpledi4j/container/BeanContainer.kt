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

import cn.caipengcheng.ssimpledi4j.exception.BeanNotFoundException
import cn.caipengcheng.ssimpledi4j.exception.ContainerNotInit
import cn.caipengcheng.ssimpledi4j.exception.DependencyCycleException
import org.slf4j.LoggerFactory

/**
 * @author Cai Pengcheng
 * Create Date: 2019-09-17
 */
private val logger = LoggerFactory.getLogger("BeanContainer")

private val beans = mutableMapOf<Class<*>, Any>()

private var isInit = false

fun beanContainerInit() {
    if (isInit) {
        logger.warn("Bean container already initialized.")
        return
    }

    getLoadedClassesOfBean().forEach { newInstance(it) }
    isInit = true
}

/**
 * 创造实例。
 */
private fun newInstance(classes: List<Class<*>>) {
    classes.forEach { newInstance(it) }
}

/**
 * 创造实例。
 */
private fun newInstance(cls: Class<*>, dependencyChain: MutableList<Class<*>> = mutableListOf()): Any {
    if (beans.containsKey(cls)) return beans[cls]!!

    dependencyChain.add(cls)

    val constructors = cls.constructors
//    if (constructors.isEmpty()) {
//        val bean = cls.getConstructor().newInstance()
//        beans[cls] = bean
//
//        return bean
//    }

    val constructor = constructors.first()
    val paramBeans = mutableListOf<Any>()

    val paramTypes = constructor.parameterTypes
    for (paramType in paramTypes) {
        if (paramType in dependencyChain) {
            logger.error("Error creating bean with name ${cls.canonicalName}: Requested bean is currently in creation.")
            throw DependencyCycleException(
                "Error creating bean with name ${cls.canonicalName}: Requested bean is currently in creation."
            )
        }

        if (paramType !in getLoadedClassesOfBean()) {
            logger.error("Error creating bean with name ${cls.canonicalName}: Requested bean ${paramType.canonicalName} is not found.")
            throw BeanNotFoundException(
                "Error creating bean with name ${cls.canonicalName}: Requested bean ${paramType.canonicalName} is not found."
            )
        }

        paramBeans.add(newInstance(paramType, dependencyChain))
    }

    val bean = constructor.newInstance(*paramBeans.toTypedArray())
    beans[cls] = bean

    return bean
}

fun <T> getInstanceFromBeans(cls: Class<T>): T {
    if (!isInit) {
        logger.error("Bean container is not initialization.")
        throw ContainerNotInit("Bean container is not initialization.")
    }

    if (cls !in getLoadedClassesOfBean()) {
        logger.error("Requested bean ${cls.canonicalName} is not found.")
        throw BeanNotFoundException(
            "Requested bean ${cls.canonicalName} is not found."
        )
    }

    return beans[cls] as T
}
