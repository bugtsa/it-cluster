package com.itcluster.mobile.app.ext.log

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

class ByteArrayBuilder private constructor(private val baos: ByteArrayOutputStream) {
    fun add(b: Byte): ByteArrayBuilder {
        baos.write(b.toInt())
        return this
    }

    private fun add(s: Short): ByteArrayBuilder {
        val iShort = s.toByte().toInt()
        baos.write(iShort)
        baos.write(iShort shr java.lang.Byte.SIZE)
        return this
    }

    fun add(c: Char): ByteArrayBuilder {
        val cInt = c.digitToInt()
        baos.write(cInt)

        baos.write(cInt shr java.lang.Byte.SIZE)
        return this
    }

    private fun add(i: Int): ByteArrayBuilder {
        add(i.toShort())
        add((i shr java.lang.Short.SIZE).toShort())
        return this
    }

    fun add(f: Float): ByteArrayBuilder {
        add(java.lang.Float.floatToIntBits(f))
        return this
    }

    fun add(l: Long): ByteArrayBuilder {
        add(l.toInt())
        add((l shr Integer.SIZE) as Int)
        return this
    }

    fun add(d: Double): ByteArrayBuilder {
        add(java.lang.Double.doubleToLongBits(d))
        return this
    }

    fun add(s: String?): ByteArrayBuilder {
        if (s != null) {
            add(s.toByteArray(StandardCharsets.UTF_8))
        }
        return this
    }

    fun add(array: ByteArray?): ByteArrayBuilder {
        if (array != null && array.isNotEmpty()) {
            try {
                baos.write(array)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        return this
    }

    fun build(): ByteArray {
        val out = baos.toByteArray()
        baos.reset()
        return out
    }

    companion object {
        fun builder(): ByteArrayBuilder {
            return ByteArrayBuilder(ByteArrayOutputStream())
        }

        fun builder(capacityHint: Int): ByteArrayBuilder {
            return ByteArrayBuilder(ByteArrayOutputStream(capacityHint))
        }
    }
}