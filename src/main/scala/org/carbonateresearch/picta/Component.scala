package org.carbonateresearch.picta

import ujson.Value

/** root trait for every graphic component that creates a chart */
private[picta] trait Component {
  /** this serializes the component to a json-like Value type. This type is used extensively for construction
   * and testing given its Monoidal property.
   */
  private[picta] def serialize: Value
}