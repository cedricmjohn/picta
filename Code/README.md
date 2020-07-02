Picta Visualization Library
============================

### Introduction

Picta is a general purpose Scala library for creating interactive charts. It is designed to be run on the JVM as well as in the Almond Notebook. 

Support for further Scala Notebook kernels, such as Zeppelin will be added in the future.

### Installation

[TODO - ONCE Final Version is Complete and Pushed to remote Repo]

### Testing

The unit tests make use of both `ScalaTest` and `Nodejs` in order to run. In order to setup the testing suite, do the following:

1.  Add `ScalaTest` to your build using `libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test"`

2. Next install `Nodejs`. More details can be found here for operating system: https://nodejs.org/en/download/

3. Next, navigate to `.../Code/src/test/resources/javascript`. Here there should be a `package.json` file. If `Nodejs` has been installed correctly, open a terminal and type `npm install` to install all the javascript library dependencies.
4. Now, simply open an `sbt` shell and type `test` and the test suite should run.

### Colors

Colors in the library are denoted by `List[String]` or by `List[Double]`. They can be composed with any object that takes in a color key.

### Lines

Lines take in `width: Double` and `color: List[String or Double]` parameters. By default the `width` of a line is set to 0.5.

**Composition**

To compose a line simply initialize it, then add to it a list of colors. If the list consists of only a single item, it will be interpreted as an individual string or double.

```scala
val line = Line() + List("rgb(255, 255, 255, 1)")
```

### Markers

Markers take in the following  optional parameters:

1. `symbol: String`
2. `color: List[String or Double]`
3. `line: Line`

**Composition**

To compose a marker, simply choose which components are required and combine them. Here we combine a symbol, `"circle"`, a color `"red"` and set the line for the marker to the default for `Line`.

```scala
val marker = Marker() + "circle" + List("red") + Line()
```

[TBU FOR REST OF COMPONENTS ONCE LIBRARY API FINALIZED]

