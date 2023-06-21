import java.time.LocalDate

object Papa{
    var regalosEnStock = mutableListOf<Regalo>()



    fun regaloAdecuado(persona: Persona) {
       return persona.regalosDeseados(regalosEnStock)
    }
}

class Persona(
    var preferencia: Preferencia,
    var marcaDeseada: String
){
    fun regalosDeseados(regalosEnStock: MutableList<Regalo>) {
        regalosEnStock.first { preferencia.regalosQueAcepta(it) }
    }
}


interface Preferencia {
    abstract fun regalosQueAcepta(regalo: Regalo): Boolean
}

// Null Object Pattern
class Conformista : Preferencia {
    override fun regalosQueAcepta(regalo: Regalo): Boolean {
        return true
    }
}

class Interesadas(var precioMinimo: Double) : Preferencia {
    override fun regalosQueAcepta(regalo: Regalo): Boolean {
        return regalo.precio > precioMinimo
    }
}

class Exigentes : Preferencia {
    override fun regalosQueAcepta(regalo: Regalo): Boolean {
        return regalo.esValioso()
    }

}

class Marqueras(var marcaDeseada : String) : Preferencia {

    override fun regalosQueAcepta(regalo: Regalo): Boolean {
        return marcaDeseada == regalo.marca
    }

}

abstract class combinetas : Preferencia {
    val preferencias: MutableSet<Preferencia> = mutableSetOf()
    override fun regalosQueAcepta(regalo: Regalo): Boolean {
        return this.preferencias.any { it.regalosQueAcepta(regalo) }
    }

    fun agregarPreferencia(preferencia: Preferencia) {
        preferencias.add(preferencia)
    }
}

abstract class Regalo() {
    var marca: String = ""
    var precio: Double = 0.0

    val listaMarcasValiosas: MutableList<String> = mutableListOf()

    open fun esValioso(): Boolean {
        return this.esCostoso() && this.miCondicion()
    }

    open fun esCostoso(): Boolean {
        return this.precio > 5000
    }

    abstract fun miCondicion(): Boolean
}

class Ropa() : Regalo() {

    override fun miCondicion(): Boolean {
        return listaMarcasValiosas.contains(this.marca)
    }
}

class Juguetes(val añoLanzamiento: Int) : Regalo() {
    override fun miCondicion(): Boolean {
        return this.añoLanzamiento < 2000
    }
}

abstract class Perfumes(var origen: Origen) : Regalo() {
    override fun miCondicion(): Boolean {
        return this.origen == Origen.EXTRANJERO
    }
}

enum class Origen() {
    NACIONAL,
    EXTRANJERO
}

abstract class Experiencias(var fechaDeEntrega: LocalDate) : Regalo() {
    override fun miCondicion(): Boolean {
        return this.esViernes()
    }

    private fun esViernes(): Boolean = this.fechaDeEntrega.dayOfWeek.value == 5
}