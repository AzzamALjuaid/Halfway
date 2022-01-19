package com.tuwaiq.halfway.utility

import junit.framework.Assert.assertEquals
import org.junit.Test

class CommonTest {

    @Test
    fun isNameFormat() {
        val result = Common.isNameFormat("azzam")
        assertEquals(result,true)
    }
}

