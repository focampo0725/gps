package com.kloubit.gps.infrastructure.utils

import android.content.Context
import android.hardware.usb.*
import android.widget.Toast
import java.util.*

class HidBridge private constructor(private val context: Context) {
    private var usbManager: UsbManager? = null
    private var usbDevice: UsbDevice? = null
    private val receivedQueue: Queue<ByteArray> = LinkedList()
    private val productId = 51
    private val vendorId = 1240
    private var deviceName: String? = null
    val RESULT_ERROR = -1

    companion object {
        private var hidBridge: HidBridge? = null
        fun getInstance(context: Context): HidBridge {
            if (hidBridge == null) hidBridge = HidBridge(context)
            return hidBridge!!
        }
    }

    fun openDevice(): Boolean {
        usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = usbManager!!.deviceList
        var deviceIterator = deviceList.values.iterator()
        usbDevice = null
        while (deviceIterator.hasNext()) {
            val device = deviceIterator.next()
            if (device.productId == productId && device.vendorId == vendorId) {
                usbDevice = device
                deviceName = usbDevice!!.deviceName
                Toast.makeText(context, "$deviceName\nPID$productId\nVID$vendorId", Toast.LENGTH_SHORT).show()
            }
        }
        if (usbDevice == null) return false
        try {
//            AutoConnectWithDeviceUsbHorizontal(context, 400, usbDevice, false)
        } catch (e: Exception) {
            e.printStackTrace()

        }
        if (usbDevice == null) {

            Log(String.format("\t I search for VendorId: %s and ProductId: %s", vendorId, productId))
            return false
        }
        Log("Encontrado el dispositivo")
        return true
    }

    fun WriteData(bytes: ByteArray): Boolean {
        try {
            if (usbDevice != null) {
                synchronized(locker) {
                    val writeIntf: UsbInterface = usbDevice!!.getInterface(0)
                    val writeEp: UsbEndpoint = writeIntf.getEndpoint(1)
                    val writeConnection: UsbDeviceConnection? = usbManager!!.openDevice(usbDevice)
                    writeConnection?.claimInterface(writeIntf, true)
                    val r: Int = writeConnection!!.bulkTransfer(writeEp, bytes, bytes.size, 0)
                    if (r != RESULT_ERROR) {
                        Log("Se escribio satisfactoriamente...")
                    } else {
                        Log("Error ocurrió mientras escribe datos. Sin ACK")
                    }
                    writeConnection.releaseInterface(writeIntf)
                    writeConnection.close()
                }
            } else {
//                logi("************************** usbDevice =  null ****************** ")
            }
        } catch (e: NullPointerException) {
//            logi("Error resultamos mientras escribía. No se pudo conectar con el dispositivo o interfaz está ocupado?")
//            logi(this.javaClass.toString() + " -> " + e.message)
            return false
        }
        return true
    }

    fun getUsbDispositivoAcoplado(): UsbDevice? {
        val deviceList = usbManager?.deviceList
        for (device in deviceList!!.values) {
            if (device.productId == productId && device.vendorId == vendorId) {
                return device
            }
        }
        return null
    }

    fun getUsbDispositivosAcoplados(context: Context): ArrayList<UsbDevice> {
        val alUsbDispositivo: ArrayList<UsbDevice> = ArrayList()
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList = usbManager.deviceList
        if (deviceList.size < 1) return alUsbDispositivo
        val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val usbDevice = deviceIterator.next()
            alUsbDispositivo.add(usbDevice)
        }
        return alUsbDispositivo
    }

    private fun Sleep(milliseconds: Int) {
        try {
            Thread.sleep(milliseconds.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun Log(message: String) {
        try {
            if (context != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }

    private fun composeString(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(b)
            builder.append(" ")
        }
        return builder.toString()
    }

    private val locker = Any()
}