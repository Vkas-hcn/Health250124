package com.jia.opens.golden.scales.towards.jgri6sd

import androidx.annotation.Keep

@Keep
data class PngJiaBean(
    val appConfig: AppConfig,
    val network: Network,
    val resources: Resources
)

data class AppConfig(
    val dataSync: Boolean,
    val exposure: Exposure,
    val timing: Timing,
    val userTier: Int
)

data class Network(
    val h5Config: H5Config
)

data class Resources(
    val delayRange: String,
    val identifiers: Identifiers
)

data class Exposure(
    val interactions: Int,
    val limits: String
)

data class Timing(
    val checks: String,
    val failure: Int
)

data class H5Config(
    val gateways: List<String>,
    val hp: String,
    val ttl: Int
)

data class Identifiers(
    val internal: String,
    val social: String
)


