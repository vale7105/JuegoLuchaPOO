# 🥋 Proyecto POO Java - Juego de Lucha 2D con LibGDX

*Elaborado por:*  
Anghelic Peñaranda  
Stefany Murillo  
Valeria Quitiaquez Pasuy 

*Asignatura:* Ingeniería de software  
*Docente:* Jhon Haide Cano Beltran  
*Curso:* 6303A

## Descripción General
Este proyecto consiste en un videojuego de lucha 2D desarrollado con Java y el framework LibGDX. Presenta una pantalla de menú y una pantalla de combate donde dos personajes (una ninja y una vaquera) se enfrentan con controles personalizados, animaciones, sonidos y detección de colisiones para ataques. Se han aplicado conceptos fundamentales de la Programación Orientada a Objetos, así como patrones de diseño para lograr una estructura de código modular y escalable.

## Conceptos Aplicados
Se implementan los cuatro pilares de la POO:

- Abstracción: A través de la clase Personaje, que define los comportamientos y propiedades generales de cualquier personaje del juego.

- Encapsulamiento: Se utilizan atributos privados en Personaje como vida, posición, textura, entre otros, con métodos públicos para su acceso o modificación.

- Herencia: Aunque todos los personajes utilizan la clase Personaje, se diferencian a través del uso del patrón Factory y parámetros personalizados.

- Polimorfismo: Cada personaje reacciona diferente a las entradas según sus controles y puede tener diferentes sprites y sonidos para el mismo comportamiento (como atacar).

## También se aplican relaciones de tipo: 

- Composición: La clase PantallaJuego compone objetos Personaje, además de la música, sonidos y elementos visuales.

- Agregación: La clase principal del juego (MainGame) agrega diferentes pantallas como PantallaMenu y PantallaJuego sin poseerlas directamente.

- Relación de uso: Las clases utilizan métodos de LibGDX como SpriteBatch, Stage, Sound, Music, y TextureRegion para renderizar gráficos y gestionar sonidos.

# Patrones de Diseño Utilizados
Factory Method: Implementado en la clase PersonajeFactory, que permite crear distintos personajes según el nombre elegido (por ejemplo: "ninja" o "vaquera") y asignarles sus texturas, animaciones y controles correspondientes.

- State Pattern (implícito): La clase Personaje representa distintos estados de animación (quieto, correr, saltar, agacharse, atacar), que cambian dinámicamente según la entrada del jugador.

- Singleton (conceptual): MainGame actúa como punto central de control del juego y mantiene la referencia activa de la pantalla actual.

- Observer Pattern (implícito): Utilizado en la detección de entradas mediante InputProcessor de LibGDX, para que el juego reaccione a las acciones del usuario.

# Estructura del Código
- MainGame: clase principal del juego, que administra las pantallas y lanza la aplicación.

- PantallaMenu: pantalla inicial que presenta el título del juego y permite comenzar con ENTER.

- PantallaJuego: contiene toda la lógica del combate, renderiza los personajes, detecta colisiones, muestra las barras de vida, administra la música y los sonidos.

- Personaje: clase que representa a cada jugador, con lógica de movimiento, animaciones, detección de daño y renderizado.

- PersonajeFactory: clase que implementa el patrón Factory para crear instancias personalizadas de personajes.

- Lwjgl3Launcher: clase específica para lanzar el juego en plataformas de escritorio.

- Assets: carpeta que contiene imágenes de fondo, avatares, sprite sheets de ataque, música y sonidos del juego.

