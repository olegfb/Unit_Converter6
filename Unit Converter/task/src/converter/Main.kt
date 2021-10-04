package converter

enum class UnitDict(val one: String, val more: String, val short: String, val strreg: Regex, val pType: String, val coef: Double) {
    M ("meter","meters", "m", "m|meters?".toRegex(), "Length", 1.0),
    KM ("kilometer", "kilometers", "km", "km|kilometers?".toRegex(), "Length", 1000.0),
    CM ("centimeter", "centimeters", "cm", "cm|centimeters?".toRegex(), "Length", 0.01),
    MM ("millimeter", "millimeters", "mm", "mm|millimeters?".toRegex(), "Length", 0.001),
    MI ("mile",  "miles", "mi", "mi|miles?".toRegex(), "Length", 1609.35),
    YD ("yard",  "yards", "yd", "yd|yards?".toRegex(), "Length",0.9144),
    FT ("foot", "feet", "ft", "ft|foot|feet".toRegex(), "Length",0.3048),
    IN ("inch", "inches", "in", "in|inch|inches".toRegex(), "Length", 0.0254),
    G ("gram", "grams", "g", "g|grams?".toRegex(), "Weight", 1.0),
    KG ("kilogram", "kilograms", "kg", "kg|kilograms?".toRegex(), "Weight", 1000.0),
    MG ("milligram", "milligrams", "mg", "mg|milligrams?".toRegex(), "Weight", 0.001),
    LB ("pound", "pounds", "lb", "lb|pounds?".toRegex(), "Weight", 453.592),
    OZ ("ounce", "ounces", "OZ", "oz|ounces?".toRegex(), "Weight",28.3495),
    DC ("degree Celsius", "degrees Celsius", "dc", "d?c|(degrees? )?celsius".toRegex(), "oftemp",0.0),
    DF ("degree Fahrenheit", "degrees fahrenheit", "df", "d?f|(degrees? )?fahrenheit".toRegex(), "oftemp",0.0),
    K ("kelvin", "kelvins", "k", "k|kelvins?".toRegex(), "oftemp",0.0),
    NONE("","???", "", "".toRegex(), "", 0.0)
}


fun main() {
    var vCountIn: Double
    do {
        print("Enter what you want to convert (or exit): ")
        val strInput = readLine()!!
        if ( strInput.lowercase() == "exit") break

        val arrStr = strInput.split(" ")
        try {
            vCountIn = arrStr[0].toDouble()
        } catch (e: Exception){
            println("Parse error\n")
            continue
        }

        var vIn = fWho(arrStr[1])
        if (vIn == UnitDict.NONE) vIn = fWho(arrStr[1]+" "+ arrStr[2])

        var vOut = fWho(arrStr[arrStr.lastIndex])
        if (vOut == UnitDict.NONE) vOut = fWho(arrStr[arrStr.lastIndex-1] + " " + arrStr[arrStr.lastIndex])


        if (vIn == UnitDict.NONE || vOut == UnitDict.NONE || (vIn.pType!=vOut.pType)) {
//            println("Conversion from ${fNvlUnit(vIn, vMeasureIn)} to ${fNvlUnit(vOut, vMeasureOut)} is impossible")
            println("Conversion from ${vIn.more} to ${vOut.more} is impossible\n")
//            println("Parse error\n")
            continue
        }

        if (vCountIn < 0.0 && vIn != UnitDict.DC) {
            println("${vIn.pType} shouldn't be negative.\n")
            continue
        }

        val rezConvert = when(vIn.pType) {
            in "Weight", "Length" -> fConvert(vCountIn, vIn, vOut)
            "oftemp" -> fConvertTemp(vCountIn, vIn, vOut)
            else -> 0.0
        }
        println("$vCountIn ${fOneMore(vCountIn, vIn)} is $rezConvert ${fOneMore(rezConvert, vOut)}\n")
    } while (true)
}


fun fConvertTemp(vCnt: Double, pIn: UnitDict, pOut: UnitDict): Double {
    return when(pIn.short+"2"+pOut.short){
        "dc2df" -> vCnt * 9 / 5 + 32
        "dc2k" -> vCnt + 273.15
        "df2dc" -> (vCnt - 32) * 5 / 9
        "df2k" -> (vCnt + 459.67) * 5 / 9
        "k2dc" -> vCnt - 273.15
        "k2df" -> vCnt * 9 / 5 - 459.67
        else -> vCnt
    }
}

fun fWho(vM: String): UnitDict{
    for (i in UnitDict.values()){
        if ((vM.lowercase().matches(i.strreg))) return i
    }
    return UnitDict.NONE
}

fun fConvert(vCnt:Double, pIn: UnitDict, pOut: UnitDict) = vCnt * pIn.coef / pOut.coef

fun fOneMore(vCnt: Double, vUn: UnitDict):String{
    return if (vCnt == 1.0) vUn.one else vUn.more
}
