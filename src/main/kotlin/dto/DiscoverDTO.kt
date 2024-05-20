package dev.jordanadams.dto

import kotlinx.serialization.Serializable

@Serializable
data class DiscoverDTO(
  val bitsToDiscover: Int,
  val needCharXpLevelCosts: Boolean,
  val needMasteryLevelCosts: Boolean,
  val storeGuid: String,
  //  not sent when making private lobby
  val needPlayerSettings: Boolean? = false,

  // Sent on first discover request?
  val movieTranscriptNumber: Int? = null,
  // If I got the Epic Games response as well, would probably just make it into an enum
  //  I can probably guess it is "epic" (lowercase, also not sure if json serializer is smart enough
  //  to automatically uppercase the string or if I need a StoreSerializer to do it for me)
  val storePlatform: String? = null,
  val journeyGuid: String? = null,
  val weaponManifestNumber: Int? = null,


  // Sent on second discover request?
  val matchmakingRegion: String? = null,

  // On making private lobby, third discover request?
  val characterTypeFilter: String? = null,

  // On Match start
  val seasonPassTranscriptNumber: Int? = null,

  override val version: Int,
  override val idpk: String
) : VHSDTO()

enum class Store {
  STEAM
}

// Deobfuscated

// Broken
// EDiscoveryDataType
@Serializable
data class ServerDiscoverDTO(
  // DDT_${Key}Bit
  // DDT_AllInventoryItemsBit
  val allInventoryItems: Int,
  val allLoadouts: Int,
  val allSceneEnactmentStates: Int,
  val allPlayerAccountPoints: Int,
  val allPlayerSlots: Int,
  val allFriendLists : Int,
  val allUnclaimedChests: Int,
  val allStoreItems: Int,
  val specificLoadouts: Int,
  val questSystem: Int,
  val seasonPass: Int,
  val allWeapons: Int,
  val journeyData: Int,
  val accountStats: Int,
  val guideSystem: Int,
  val entitlements: Int,
  val patchNotes: Int,
  val seasonalEvent: Int,
  val serverNotification: Int,
  val dynamicBountyRewards: Int,
  val communityGoals: Int,

  // accountIdToDiscover
  // discoverKey
  // padLoadout
  // Store Platform
  // matchmakingRegion
  // storeGuid
  // Discover
  // characterLoadouts
  // teenAffinities
  // charXpLevelCosts
  // playerSettingsData
  // playerAccountPoints
  // masteryLevelCosts
  // playerAccountSlots
  // serverTime
  // serverNotificationData

  override val version: Int,
  override val idpk: String
) : VHSDTO()