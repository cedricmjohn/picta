/* Author:          Fazal Khan
   Github:          acse-fk4517
   Github Repo:     https://github.com/acse-2019/irp-acse-fk4517
*/
package org.carbonateresearch.picta.options

/** This specifies how the errors for an error bar are calculated. */
private[picta] trait ErrorMode

/** The error bar length corresponds to a percentage of the underlying data. */
case object PERCENT extends ErrorMode

/** The error bar length corresponds to a constant value. */
case object CONSTANT extends ErrorMode

/** The error bar length corresponds to the square root of the underlying data. */
case object SQRT extends ErrorMode

/** The error bar length corresponds the value set by the user. */
case object DATA extends ErrorMode
