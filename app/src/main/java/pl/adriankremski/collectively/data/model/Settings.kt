package pl.adriankremski.collectively.data.model

data class Settings(val emailSettings: SettingsEntry)

data class SettingsEntry(
        val enabled: Boolean,
        val remarkCreated: Boolean,
        val remarkCanceled: Boolean,
        val remarkDeleted: Boolean,
        val remarkProcessed:Boolean,
        val remarkRenewed: Boolean,
        val remarkResolved: Boolean,
        val photosToRemarkAdded: Boolean,
        val commentAdded: Boolean
)
