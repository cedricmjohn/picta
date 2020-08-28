/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options.histogram

/** This trait acts as an ENUM for direction option. Only applies if cumulative is enabled. */
sealed trait Direction
/** Sum prior bins so that the result increases from left to right. */
case object INCREASING extends Direction

/** Sum later bins so that the result decreases from left to right. */
case object DECREASING extends Direction