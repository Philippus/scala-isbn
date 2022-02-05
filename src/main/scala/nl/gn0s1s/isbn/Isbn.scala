package nl.gn0s1s.isbn

final case class Isbn private (value: String) {
  lazy val Array(prefixElement, registrationGroupElement, registrantElement, publicationElement, checkDigit) =
    toHyphenatedIsbn13.split('-')

  def toIsbn10: Option[String] =
    if (value.startsWith("978"))
      Some(Isbn.isbn13toIsbn10(value))
    else
      None

  def toIsbn13: String = value

  def toAmazonStyleHyphenatedIsbn13: String = s"${value.take(3)}-${value.drop(3)}"

  def toHyphenatedIsbn10: Option[String] =
    if (value.startsWith("978")) {
      Some(toHyphenatedIsbn13.drop(4).init ++ Isbn.isbn13toIsbn10(value).last.toString)
    } else
      None

  def toHyphenatedIsbn13: String = {
    for {
      ucc <- Isbn.rangeMessage \ "EAN.UCCPrefixes" \ "EAN.UCC"
      uccPrefix <- ucc \ "Prefix" if value.startsWith(uccPrefix.text)
      uccRule <- ucc \ "Rules" \ "Rule"
      uccRange <- uccRule \ "Range"
      Array(start, end) = uccRange.text.split('-') if start.toInt to end.toInt contains value
        .substring(uccPrefix.text.length, uccPrefix.text.length + start.length)
        .toInt
      uccLength <- uccRule \ "Length" if uccLength.text.toInt > 0
      group <- Isbn.rangeMessage \ "RegistrationGroups" \ "Group"
      prefix <- group \ "Prefix" filter (node =>
        node.text == s"${uccPrefix.text}-${value.substring(uccPrefix.text.length, uccPrefix.text.length + uccLength.text.toInt)}"
      )
      rule <- group \ "Rules" \ "Rule"
      range <- rule \ "Range"
      Array(start, end) = range.text.split('-') if start.toInt to end.toInt contains value
        .substring(prefix.text.filterNot(_ == '-').length, prefix.text.filterNot(_ == '-').length + start.length)
        .toInt
      length <- rule \ "Length" if length.text.toInt > 0
    } yield s"${prefix.text}-${value.substring(prefix.text.filterNot(_ == '-').length, prefix.text.filterNot(_ == '-').length + length.text.toInt)}-${value
        .substring(prefix.text.filterNot(_ == '-').length + length.text.toInt, value.length - 1)}-${value.last}"
  }.headOption.getOrElse(value)
}

object Isbn {
  private val file = scala.io.Source.fromResource("RangeMessage.xml").reader()
  private lazy val rangeMessage = scala.xml.XML.load(file)

  def apply(value: String): Option[Isbn] = {
    val candidate = value.filterNot(ch => ch == '-' || ch == ' ')
    if (isValidIsbn13(candidate)) // isbn-13
      Some(new Isbn(candidate))
    else if (isValidIsbn10(candidate)) // isbn-10
      Some(new Isbn(isbn10toIsbn13(candidate)))
    else if (isValidIsbn10(s"0$candidate")) // sbn
      Some(new Isbn(isbn10toIsbn13(s"0$candidate")))
    else
      None
  }

  private def isbn13toIsbn10(isbn13: String): String = {
    val isbn10Prefix = isbn13.substring(3, 12)

    s"${isbn10Prefix}${calculateCheckDigitForIsbn10(isbn10Prefix)}"
  }

  private def isbn10toIsbn13(isbn10: String): String = {
    val isbn13Prefix = "978" + isbn10.init

    s"${isbn13Prefix}${calculateCheckDigitForIsbn13(isbn13Prefix)}"
  }

  private def isValidIsbn13(isbn13: String): Boolean =
    isbn13.length == 13 && isbn13.forall(_.isDigit) && calculateCheckDigitForIsbn13(isbn13) == isbn13.last

  private def isValidIsbn10(isbn10: String): Boolean =
    isbn10.length == 10 && isbn10.init.forall(_.isDigit) && calculateCheckDigitForIsbn10(isbn10) == isbn10.last

  private def calculateCheckDigitForIsbn13(isbn13: String): Char = {
    val isbn13Prefix = isbn13.take(12)
    val res = isbn13Prefix
      .map(_.asDigit)
      .zip(List.fill(6)(Seq(1, 3)).flatten)
      .foldLeft(0)((z, x) => z + (x._1 * x._2))

    10 - res % 10 match {
      case 10 => '0'
      case digit => (digit + 48).toChar
    }
  }

  private def calculateCheckDigitForIsbn10(isbn10: String): Char = {
    val isbn10Prefix = isbn10.take(9)
    val res = isbn10Prefix
      .map(_.asDigit)
      .zip(10 to 1 by -1)
      .foldLeft(0)((z, x) => z + (x._1 * x._2))

    (11 - res % 11) % 11 match {
      case 10 => 'X'
      case digit => (digit + 48).toChar
    }
  }
}
