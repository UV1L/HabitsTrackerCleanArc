package com.example.habitstracker.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.data.HabitsRepositoryImpl
import com.example.data.RetrofitService
import com.example.data.db.HabitDb
import com.example.data.type_adapters.HabitAdapter
import com.example.domain.AddHabitsUseCase
import com.example.domain.AddHabitsUseCaseImpl
import com.example.domain.entities.Habit
import com.google.gson.GsonBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ProviderModule {

    @Provides
    fun provideMainUseCase(mainRepository: HabitsRepositoryImpl): AddHabitsUseCase {
        return AddHabitsUseCaseImpl(mainRepository)
    }

    @Provides
    fun provideMainRepository(retrofitService: RetrofitService, db: HabitDb): HabitsRepositoryImpl {
        return HabitsRepositoryImpl(retrofitService, db)
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
        return retrofit.create(RetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val gsonHabitConverter = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://habits-internship.doubletapp.ai/swagger/index.html/")
            .addConverterFactory(GsonConverterFactory.create(gsonHabitConverter))
            .build()

        return retrofit
    }

    @Singleton
    @Provides
    fun provideHabitDao(context: Context): HabitDb =
        Room.databaseBuilder(
            context,
            HabitDb::class.java, HabitDb.NAME
        )
            .fallbackToDestructiveMigration()
            .build()
}

@Singleton
@Component(modules = [ProviderModule::class])
interface ApplicationComponent {

    fun inject(): AddHabitsUseCase

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setContext(context: Context): Builder

        fun build(): ApplicationComponent
    }
}

class MyApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent
            .builder()
            .setContext(applicationContext)
            .build()
    }
}