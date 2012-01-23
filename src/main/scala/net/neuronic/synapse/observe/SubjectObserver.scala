package net.neuronic.synapse.observe

import net.neuronic.synapse.data._

trait Subject {
  private var observers: List[Observer] = List();

  def subscribe(obs: Observer) = {
     observers = obs :: observers
  }

  def publish(o: Observation) = {
     for (obs <- observers)
       obs.notify(o)
  }
}

trait Observer {
  def notify(o:Observation) = println("subject observed")
}

trait SubjectObserver extends Subject with Observer
