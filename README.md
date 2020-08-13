# Picta

### A graphing library for the Scala Programming language

Picta is a graphing library that allows interactive data exploration in the Scala ecosystem. 

Picta has an easy to use API that is as close to natural language as possible, making it easy to create plots while exploring data. 

Picta also allows many different plot types, adding extensive charting functionality to the Scala ecosystem.

## Contents 

[Requirements](#requirements)

[Installation](#installation)

[Documentation](#documentation)

## Introduction

Picta was written from scratch, except for the following files:

- ```src/main/resources/macy.min.js```
- ```src/main/resources/merge-image.js```
- ```src/main/resources/plotly.min.js```
- ```src/main/resources/require.min.js```

These files simply contain code from open source libraries used in the creation of Picta.

## Requirements

To use this project, you need to have installed Scala 2.12.12+.

##### Running The Unit Testing Framework

If you wish to run the unit tests, you will also need to install [Node.js](https://nodejs.org/en/). 

If you have NPM installed, you can simply navigate to ```/src/test/resources/javascript/``` and run the following command:

```shell
npm install
```

This should install all the necessary dependencies to get the unit-testing framework working.

To run the tests, simply open an SBT shell and use the following command:

```scala
+ test
```

This will run the unit tests, which should all pass.

## Installation

#### SBT

If you are using SBT, add the following to your build.sbt file:

```scala
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies  ++= Seq(
  // Last  release
  "org.carbonateresearch" %% "picta" % "0.1.1"
)
```

### Almond Jupyter Notebook

To use inside an Almond Jupyter Notebook, add the following to the top of the notebook:

```scala
interp.repositories() ++= Seq(coursierapi.MavenRepository.of(
  "https://jitpack.io"
))

import $ivy. `org.carbonateresearch::picta:0.1.1`
```

## Documentation

The documentation site for the project can be found here:

[Documentation Site](https://acse-fk4517.github.io/picta-docs/index.html)

The documentation site contains extensive examples on how to use the Picta library.

The documentation is built with [Jekyll](https://jekyllrb.com), using the theme by Tom Johnson [here](https://github.com/tomjoht/documentation-theme-jekyll)

In addition to the documentation site above, there is also an API site, built with [Scaladoc](https://docs.scala-lang.org/overviews/scaladoc/for-library-authors.html) here:

[Picta API](https://acse-fk4517.github.io/picta-api/)

The API docs contain detailed information about the source code, such as function signatures and types of the various elements in the library.

You can find an extensive list of plot examples [here](https://acse-fk4517.github.io/picta-docs/pages/Picta-Examples-Notebook.html)


## Credit

The Picta logo was created by [design_league](https://www.fiverr.com/design_league).

















