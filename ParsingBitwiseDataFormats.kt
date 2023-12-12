import kotlin.experimental.and

data class SensorData (
  val sequenceNo: UByte,
  val temperature: Byte,
  val humidity: UByte,
  val pressure: UShort,
  val errCode: UByte
)

fun main() {
  val receivedSensorData = byteArrayOf(
    0b00000000,
    0b11111111.toByte(),
    0b01010001,
    0b00001110.toByte(),
    0b00000000
  )

  val sequenceNo = receivedSensorData[0].toUByte()
  println("seqNo: $sequenceNo")
  // Output: seqNo: 0

  val temperature = receivedSensorData[1]
  println("temperature: $temperature")
  // Output: temperature: -1

  val tmp = receivedSensorData[2]
  // import kotlin.experimental.and が必須
  val humidity = ((tmp.toInt() ushr 1).toByte() and 0b01111111.toByte()).toUByte()
  println("humidity: $humidity")
  // Output: humidity: 40

  val tmp2 = receivedSensorData[3]
  val tmp3 = receivedSensorData[4]

  val msb = (tmp and 1.toByte()).toInt() shl 10
  val tmp4 = (tmp3.toInt() ushr 6).toByte() and 0b00000011.toByte()
  val pressure = ((msb or (tmp2.toInt() shl 2)) or tmp4.toInt()).toUShort()
  println("pressure: $pressure")
  // Output: pressure: 1080

  val errCode = (tmp3 and 0b00111111.toByte()).toUByte()
  println("errCode: $errCode")
  // Output: errCode: 0

  val buildSensorData = SensorData(
    sequenceNo=sequenceNo,
    temperature=temperature,
    humidity=humidity,
    pressure=pressure,
    errCode=errCode
  )
  println(buildSensorData)
  // Output: SensorData(sequenceNo=0, temperature=-1, humidity=40, pressure=1080, errCode=0)
}
