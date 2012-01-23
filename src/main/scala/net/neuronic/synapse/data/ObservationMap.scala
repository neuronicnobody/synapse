package net.neuronic.synapse.data

import net.neuronic.synapse.observe._
import net.neuronic.synapse.detect._
import scala.collection.mutable.{ListBuffer, HashMap}

class ObservationMap[T] {
  val data = new HashMap[Symbol, ListBuffer[T]]()

  def record(k:Symbol, o:T) = {
    (data get k) match {
      case Some(b) => b += o 
      case None    => data.put(k, ListBuffer[T]()) 
    }
  }
  
  def contains(k:Symbol):Boolean = data contains k

  def get(k:Symbol):ListBuffer[T] = {
    (data get k) match {
      case Some(b) => Nil 
      case None    => data.put(k, ListBuffer[T]())
    }
    data(k)
  }
}
