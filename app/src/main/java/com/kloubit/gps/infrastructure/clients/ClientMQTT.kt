package com.kloubit.gps.infrastructure.clients

import android.content.Context
import androidx.lifecycle.Observer
import com.abx.mqtt_client.client.ClientMQTTDriver
import com.abx.mqtt_client.client.MQTTState
import com.abx.mqtt_client.client.MqttClientType
import com.abx.shared.supportabx.dto.GenericEvent
import com.abx.shared.supportabx.dto.GenericStateEvent
import com.abx.shared.supportabx.extensions.doAsynTask
import com.abx.shared.supportabx.extensions.logi
import com.google.gson.Gson
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.domain.annotations.UIThread
import com.kloubit.gps.domain.dto.ABROKDTO
import com.kloubit.gps.infrastructure.business.IMQTTCommand
import com.kloubit.gps.infrastructure.business.IPayload
import com.kloubit.gps.infrastructure.extensions.logerror
import com.kloubit.gps.infrastructure.extensions.logi
import com.kloubit.gps.infrastructure.stateful.AppState
import org.eclipse.paho.client.mqttv3.MqttConnectOptions


/**
 * Client mqtt responsable de mantener la conexiòn constante con el broker mosquitto.
 */

class ClientMQTT(
    val appState: AppState,
    val context: Context,
    val appRepository: AppRepository
) : IClient,
    ClientMQTTDriver.IGenericStateEvent, ClientMQTTDriver.IGenericEvent {

    companion object {
        private val START_CONNECTION_DELAY: Long = 1000
        var isMQTTClientConnected = false
    }
    var password ="admin"
    var clientMQTTDriver: ClientMQTTDriver? = null
    var payloadObserver: Observer<IPayload>? = null




    //    var payloadObserver: Observer<IPayload>? = null
    private fun options(): MqttConnectOptions {
        val options = MqttConnectOptions()
        options.keepAliveInterval = 30
        options.isCleanSession = true
        options.isAutomaticReconnect = true
        options.userName ="admin"
//            appState.appParamsDTO?.brokerParamsDTO?.brokerUsername // "admin"  // todo << HERE CREDENTIAL USERNAME
        options.password = password.toCharArray()
//            appState.appParamsDTO?.brokerParamsDTO?.brokerPassword?.toCharArray() // "admin".toCharArray()    // todo << HERE CREDENTIAL PASSWORD

        options.connectionTimeout = 10
        options.keepAliveInterval = 30
        // Establecer un callback para manejar eventos de reconexión
        options.maxReconnectDelay = 5000 // Retardo máximo entre intentos de reconexión
        return options
    }

    val mqttCommands : Map<String, IMQTTCommand> = mapOf(

    )

    val mqttCommandsGenerics : Map<String, IMQTTCommand> = mapOf(


    )

    @UIThread
    override fun onEvent(genericEvent: GenericEvent) {
        when (genericEvent.name()) {
            ClientMQTTDriver.originIndividualTopic() -> {
                try {
                    val dataFromBroker = genericEvent.data().firstOrNull()

                    context.logi("eventname : ${genericEvent.name()}\ndataFromBroker : $dataFromBroker")
                    val brokerMQTTDTO =
                        Gson().fromJson(dataFromBroker as String, ABROKDTO::class.java)
                    mqttCommands[brokerMQTTDTO.hd.cmd]?.let {
                        it.invoke(appState = appState, context = context, appRepository = appRepository, abrokdto = brokerMQTTDTO)
                    }
                } catch (e: Exception) {
                    logerror("eventname: ${e.message}")
                    e.printStackTrace()
                }
            }
            ClientMQTTDriver.originGeneralTopic() -> {
                context.logi("eventname : ${genericEvent.name()}\n broadcast")
                try {

                    val dataFromBroker = genericEvent.data().firstOrNull()

                    context.logi("eventname : ${genericEvent.name()}\ndataFromBroker : $dataFromBroker")
                    context.logi("[ExceptionJson] JSON before parsing: $dataFromBroker")
                    val brokerMQTTDTO = Gson().fromJson(dataFromBroker as String, ABROKDTO::class.java)
                    mqttCommandsGenerics[brokerMQTTDTO.hd.cmd]?.let {
                        it.invoke(appState = appState, context = context, appRepository = appRepository, abrokdto = brokerMQTTDTO)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @UIThread
    override fun onStateEvent(genericStateEvent: GenericStateEvent) {
        context.logi("[ClientMQTT] MQTT onStateEvent says : ${genericStateEvent.state().getStateName()}")
        isMQTTClientConnected = (genericStateEvent.state().getStateName() == MQTTState.CONNECTED.getStateName())
        context.logi("[ClientMQTT] MQTT onStateEvent says 01 : ${isMQTTClientConnected}")
        context.logi("[ClientMQTT] MQTT onStateEvent says 02 : ${genericStateEvent.state().getStateName()}")
        if (genericStateEvent.state().getStateName() == MQTTState.CONNECTED.getStateName())
            context.logi("Conexion exitosa")
//            appState.applicationIndicatorsState.postDelay(Pair(AppIndicatorStateConstants.MQTT_CONNECTION_STATE, AppIndicatorStateConstants.MQTT_CONNECTION_STATE_CONNECTED), 200)
//
        if (genericStateEvent.state().getStateName( ) == MQTTState.FAILURE.getStateName() || genericStateEvent.state().getStateName() == MQTTState.CONNECTION_LOST.getStateName())
            context.logi("Conexion fallida")
//            appState.applicationIndicatorsState.postDelay(Pair(AppIndicatorStateConstants.MQTT_CONNECTION_STATE, AppIndicatorStateConstants.MQTT_CONNECTION_STATE_DISCONNECTED), 200)

    }

    override fun onStart(context: Context) {
        try {


            if (clientMQTTDriver == null) {

                clientMQTTDriver = ClientMQTTDriver.Builder()
                    .setContext(context)
                    .setHostname("tcp://serverdevops.abexa.pe:1883")
                    .setMqttConnectionOptions(options())
                    .setMqttClientType(MqttClientType.IAMETRIX_MOBILE)
                    .setClientId("Franco")
//                    .setClientId(DeviceUtils.getClientId(appState.device!!))
                    .setOnSocketEvent(this)
                    .setOnSocketState(this)
                    .create()
            }

//        build more data.. : delay por el hecho de q los livedata puedan tardarse en obtener datos y vehicle se encuentre NULL
            doAsynTask({
                Thread.sleep(START_CONNECTION_DELAY)
                true
            }, {
                clientMQTTDriver?.startClient()
            })

            payloadObserver = Observer {
                try {

                    logi("---------------- enviando por mqtt ---------------- ${it.topicName} ${it}")
                    logi("topicoo ; ${clientMQTTDriver!!.getCurrentTopic()}")

                    clientMQTTDriver?.publishMessage(
                        msg = it.toString(),
                        topic = it.topicName,
                        qos = 1
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            appState.payloadMutableLiveData.observeForever(payloadObserver!!)
        } catch (e: Exception) {
            logerror("----------- ERROR DE MQTT CONNECTION ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        clientMQTTDriver?.stopClient()
        appState.payloadMutableLiveData.removeObserver(payloadObserver!!)
        clientMQTTDriver = null   // todo added
    }


    /**
     *
     * Estos son para realizar testeos por CMD de mosquitto
    mosquitto subscription :

    //exportar base de datos
    mosquitto_sub.exe -h serverdevops.abexa.pe -p 1883 -t ABX/MOBILE/IAMETRIX/RQ -u admin -P admin
    mosquitto_pub.exe -h serverdevops.abexa.pe -p 1883 -t ABX/MOBILE/IAMETRIX/354061761796196/RQ -u admin -P admin -m "{\"hd\":{\"uid\":\"40158\",\"tr\":\"nulito\",\"cmd\":\"EXPORT_DATABASE_COMMAND\",\"ctype\":\"FIRE_AND_FORGET\",\"origin\":\"BM.0000000002\",\"destiny\":\"BD.0000000001\",\"seq\":1,\"seqt\":1},\"bd\":{\"deviceLinkCode\":123456789,\"createAt\":1649786345000},\"ft\":{\"ver\":\"1.0.0\",\"sig\":\"null\"}}"

     */


}



