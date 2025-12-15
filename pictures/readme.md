# MobileTemplate (Light v1)
#### Descripción
Plantilla móvii trabajada con kotlin en android studio con el propòsito de optimizar el tiempo de desarrollo en cuanto a su mantenimiento, para ello se optò por un patròn de arquitectura MVVM con el uso de DaggerHilt para el manejo de las dependencias.

#### Características
- Contenedor de versiòn de librerìas en buildSrc
- Creaciòn de mòdulos con daggerhilt
- Uso de retrofit, room con rxjava
- Extensiones como utilitarios


#### Estructura
| Capa | #Descripciòn |
| :---: | :---: |
| [**data**](#) | Contenedor de todos los **proveedores de datos** como : retrofit/network, file txt, spf, room
| [**di**](#) | Contenedor de definiciones para las implementaciones de dependencias
| [**domain**](#) | Contenedor para la lògica de negocio : entidades, pojos, DTO's y algunos de sus servicios como (criptografìa de objetos o paràmetros propios de las clases ya mencionadas)
| [**ui/presentation**](#) | Contenedor de las **actividades, fragments, dialogs, custom widgets, adapter**. Todo aquello que serà utilizado para el renderizado y/o controladores como **view models**
| [**infrastructure**](#) | Contenedor de implementaciones propias ya del framework android como : Services, Receivers, Locations, Drivers de perifèricos




# Hilos abiertos de referencias sobre las capas como infraestructura
#### Discussion about "where should are the Services (Notifications, Locations, etc) in clean architecture"
>> @donlucard

Hi,

can you share your thoughts where should framework specific stuff be placed. For example I have to integrate following:

GCM (push notifications) - regular push notifications with news
Google Analytics - tracking for pageview's and events
Where should GCM code reside (InstanceIDListenerService and GcmListenerService)? How would you handle processing of incoming notifications/payload.

Where would you place tracking related classes and should we call tracker from presenter or usecase?

>> @Trikke

Hi @donlucard ,

This depends on the scope of your project.

In smaller projects, stuff like this ends up in the Data layer. The interfaces to these infrastructures would be part of the Domain layer, but the implementation live in the Data layer. You could argue that you're still mainly handling data, so that's we i'd put it there. For analytics as an example, you would have a TrackEventUseCase with some parameters, which would flush these events to a cache in the Data layer. Once the cache is full, they are then flushed to Google Analytics. Or for example, if your app goes to the background, then all events are send to Google Analytics. This of course depends on your own requirements.

For larger projects, stuff like this could end up in a separate Infrastructure layer. The interfaces to these infrastructures would be part of the Domain layer, but the implementation live in the Infrastructure layer. Larger projects could end up being used on several platforms, so implementations for Push notifications and such would be different.

More info :
https://github.com/android10/Android-CleanArchitecture/issues/127

#### Conclusion about "how manage the services in clean architecture"
Los componentes propios ya del framework mismo (servicios, locationmanager, content provider, etc) se sugieren ser manejados en la capa de infraestructura por las siguientes razones :

- Esta capa de infraestructura es la encargada de implementar la capa de DOMINIO (interfaces como EntityDataSource) con los drivers propios del framework android (Room, Paper, etc)
- La capa DATA no debe màs que tener definiciones abstractas del flujo de datos.
- Al final el mòdulo CORE debe no depender de un framework especìfico (android, web, desktop) sino debe ser acoplable con cualquier otro framework màs.
- El mòdulo CORE debe ser solo una librerìa de java y no una librerìa de android.

More Info :
https://github.com/android10/Android-CleanArchitecture/issues/151
https://dagger.dev/hilt/components.html


## License

MIT
 