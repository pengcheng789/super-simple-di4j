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

import cn.caipengcheng.ssimpledi4j.annotation.Component
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Cai Pengcheng
 * Create Date: 2019-09-18
 */
class GetInstanceTest {
    @Component
    class A {
        val a = "a"
    }

    @Component
    class B(val a: A) {
        val b = "b"
    }

    @Test
    fun testGettingInstance() {
        ssimpleDI4jInit(GetInstanceTest::class.java)
        val a = getInstance(A::class.java)
        val b = getInstance(B::class.java)

        assertEquals(a.a, "a")
        assertEquals(b.b, "b")
        assertEquals(b.a, a)
    }
}