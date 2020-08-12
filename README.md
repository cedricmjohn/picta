# Picta

---

### A graphing library for the Scala Programming language

## Contents 

---

[Requirements](#requirements)

[Installation](#installation)

[Documentation](#documentation)

## Introduction

---

Picta is a graphing library that allows interactive data exploration in the Scala ecosystem. Picta has an easy to use API, that is close to natural language as possible, and adds extensive charting functionality to the Scala ecosystem.

Picta was written from scratch, except for the following files:

- ```src/main/resources/macy.min.js```
- ```src/main/resources/merge-image.js```
- ```src/main/resources/plotly.min.js```
- ```src/main/resources/require.min.js```

## Requirements

---

To use this project, you need to have installed Scala 2.12.12+.

If you wish to run the unit-tests, you will also need to install [Node.js](https://nodejs.org/en/). 

If you have NPM installed, you can simply navigate to ```/src/test/resources/javascript``` and run the following command:

```shell
npm install
```

This should install install all the necessary dependencies to get the unit-testing framework working.

## Installation

---

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

---

Full documentation for the project can be found here:

[Documentation Site](https://acse-fk4517.github.io/picta-docs/index.html)

The documentation site contains examples on how to use the Picta library, including how to run the unit-testing framework.

The documentation is built with [Jekyll](https://jekyllrb.com), using the theme by Tom Johnson [here](https://github.com/tomjoht/documentation-theme-jekyll)

In addition to the documentation site above, there is also an API site, built with [Scaladoc](https://docs.scala-lang.org/overviews/scaladoc/for-library-authors.html) here:

[Picta API](https://acse-fk4517.github.io/picta-api/)

The API docs contain detailed information about the source code, such as function signatures and types of the various elements in the library.

## Credit

The Picta logo was designed by [design_league](https://www.fiverr.com/design_league).

















