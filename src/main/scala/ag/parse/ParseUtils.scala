package ag.parse

object ParseUtils {
  def split(str: String, separator: String): List[String] = str.split(separator).toList.collect {
    case s: String if s.trim.length > 0 => s.trim
  }
}
