# Picta

### A graphing library for the Scala Programming language

Picta is a graphing library for the Scala. It has an easy to use API that is close to natural language, making it easy to visualize different types of data.

Picta comes with many different plot types, adding extensive charting functionality to the Scala ecosystem.

Plots are composed from smaller, simpler components, and combined together to create plots of varying complexity.

The library is written in Scala, and cross-compiles to versions 2.12 and 2.13.

Picta is also able to run inside a Jupyter Notebook, thanks to it's integration with the [Almond](https://almond.sh) kernel.

All plots created by Picta are valid HTML. More information about the library can be found at the [Documentation](#documentation) site.

## Contents

[Repository Structure](#repository-structure)

[Maven Repository](#maven-repository)

[Requirements](#requirements)

[Installation](#installation)

[Running The Unit Testing Framework](#running-the-unit-testing-framework)

[Documentation](#documentation)

## Repository Structure

The following are the main folders and files for the project. Files or folders not listed here are simply used for compilation, and can be ignored.

### 1. /src/

This folder contains all the main code for the library, and consists of of two further directories:

#### /src/main/

This folder contains all the code that implements Picta. In this folder you will find the case classes that represent each plot component.

All code associated with actually rendering plots on screen can be found in the `/render/` folder.

The `/conus/` folder contains code for all the Picta helper functions that help a user visualize forward-models.

The remaining folders implement various functionality useful to the library.

#### /src/test/

This folder contains the unit tests for the library. Generally each class corresponds to a test suite that tests a specific part of the library.

To see how to set up the unit testing framework, please see [Running The Unit Testing Framework](#running-the-unit-testing-framework) below.

#### /src/test/resources

This folder contains the Javascript files that are used to run the Node.js specific unit testing code.

- `node_modules` contains all the dependencies downloaded by the `NPM` package manager to run the Node.js specific unit testing code.

### 2. /Documentation/

This folder simply contains a README.md file that contains details on where to find the in depth documentation.

### 3. /build.sbt

This file tells `SBT` how to build and compile the project. The file also contains all the dependencies this project will automatically pull in when
it is being compiled and built.

### 4. COPYING.md

This contains a copy of the GNU general public license, version 3.

### 5. license.md

This contains information on how the project is licensed. It goes hand in hand with the `COPYING.md` listed file above.

### 6. Picta-Examples-Notebook.ipynb

This is a Jupyter notebook that contains many different examples of charts that are created using the Picta library.

### 7. rootdoc.txt

This file is used to create the start page for the API documentation, which can be found hosted at [Picta API](https://acse-fk4517.github.io/picta-api/).

### 8. Other Files

Picta was written from scratch, except for the following files:

- `src/main/resources/macy.min.js`
- `src/main/resources/merge-image.js`
- `src/main/resources/plotly.min.js`
- `src/main/resources/require.min.js`

These files simply contain code from open source libraries used in the creation of Picta.

## Maven Repository

This library is hosted on Maven for an easy installation on your machine.

You can find the Maven repository: [https://mvnrepository.com/artifact/org.carbonateresearch/picta](https://mvnrepository.com/artifact/org.carbonateresearch/picta)

## Requirements

To use this project, you need to have installed Scala 2.12.12+.

## Running The Unit Testing Framework

If you wish to run the unit tests, you will also need to install [Node.js](https://nodejs.org/en/).

If you have NPM installed, you can simply navigate to `/src/test/resources/javascript/` and run the following command:

```shell
npm install
```

This should install all the necessary dependencies to get the unit-testing framework working.

To run the tests, simply open an SBT shell and use the following command:

```scala
+ test
```

This will run the unit tests, which should all pass.

You can find more information on how to contribute using the unit-testing framework in the main documentation [here](https://acse-fk4517.github.io/picta-docs/contributing_library).

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

#### Main Documentation Site

The documentation site for the project can be found here:

- [Documentation Site](https://acse-fk4517.github.io/picta-docs/index.html)

The documentation site contains extensive examples on how to use the Picta library.

The documentation is built with [Jekyll](https://jekyllrb.com), using the theme by Tom Johnson [here](https://github.com/tomjoht/documentation-theme-jekyll).

#### API Documentation

In addition to the documentation site above, there is also an API documentation site, built with [Scaladoc](https://docs.scala-lang.org/overviews/scaladoc/for-library-authors.html) here:

- [Picta API](https://acse-fk4517.github.io/picta-api/)

The API docs contain detailed information about the source code, such as function signatures and types of the various elements in the library.

#### A Long List of Examples

You can find an extensive list of plot examples [here](https://acse-fk4517.github.io/picta-docs/pages/Picta-Examples-Notebook.html).

## Credit

The Picta logo was created by [design_league](https://www.fiverr.com/design_league).
