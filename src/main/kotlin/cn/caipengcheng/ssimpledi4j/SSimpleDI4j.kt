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

package cn.caipengcheng.ssimpledi4j

import cn.caipengcheng.ssimpledi4j.container.beanContainerInit
import cn.caipengcheng.ssimpledi4j.container.classContainerInit
import cn.caipengcheng.ssimpledi4j.container.getInstanceFromBeans

/**
 * @author Cai Pengcheng
 * Create Date: 2019-09-17
 */

fun ssimpleDI4jInit(mainClass: Class<*>) {
    classContainerInit(mainClass)
    beanContainerInit()
}

fun <T> getInstance(cls: Class<T>): T {
    return getInstanceFromBeans(cls)
}