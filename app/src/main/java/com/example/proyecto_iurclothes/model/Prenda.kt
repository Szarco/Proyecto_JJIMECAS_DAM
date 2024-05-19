package com.example.proyecto_iurclothes.model

import java.io.Serializable
//Objeto prenda con variables a guardar
class Prenda (var id:Int, var imagen: String, var tipo: String, var temporada: String, var moda: String, var color: String, var favorito: Boolean):Serializable{

    constructor() : this(0,"", "", "", "", "", false)

}

