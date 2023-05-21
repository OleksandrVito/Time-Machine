package ua.vitolex.timecalculator.di

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.vitolex.timecalculator.TimeCalculatorApp
import ua.vitolex.timecalculator.settings.UserSettings
import ua.vitolex.timecalculator.settings.UserSettingsImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): TimeCalculatorApp{
        return  app as TimeCalculatorApp
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindUserSettings(
        userSettingsImpl: UserSettingsImpl
    ): UserSettings
}