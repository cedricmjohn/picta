Picta Visualization Library
============================

### Introduction

Picta is a general purpose Scala library for creating interactive charts. It is designed to be run on the JVM as well as in the Almond Notebook. 

Support for further Scala Notebook kernels, such as Zeppelin will be added in the future.

### Installation









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

