package net.neuronic.synapse.detect

import net.neuronic.synapse.observe._
import net.neuronic.synapse.data._
import net.neuronic.synapse.visualize._

import scala.collection.mutable.ListBuffer

class Detector[T, K](peers:DetectorCollection, detect: (T, ListBuffer[T]) => Option[K])(implicit m:Manifest[T]) extends SubjectObserver {
  private val omap = new ObservationMap[T]
  subscribe(peers)
  override def notify(o:Observation) = {
    if(m.erasure == o.getClass) {
      detect(o.asInstanceOf[T], omap.get(o.series)) match {
        case Some(t) => publish(t.asInstanceOf[Observation])
        case None    => Nil
      }
    }
  }
}
