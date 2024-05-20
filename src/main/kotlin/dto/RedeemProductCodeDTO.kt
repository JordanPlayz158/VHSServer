package dev.jordanadams.dto

import kotlinx.serialization.Serializable

@Serializable
data class RedeemProductCodeDTO(
  val sessionTicketId: String,
  val productCode: String,
  override val version: Int,
  override val idpk: String
) : VHSDTO()