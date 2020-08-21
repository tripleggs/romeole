package org.romeole.data.script
import java.util.{HashMap => java_hash_map}

object HelloScala {

  def main(args: Array[String]): Unit = {
    var map = new java_hash_map[String, String]()
    var key : String = "hello"
    var value = "scala"
    map.put(key, value)
    println(map)
    println(mult(23))
  }

  var mult = (i: Int) => i * 10

}
