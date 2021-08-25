package com.example.habitstracker.application

import android.app.Application
import com.example.data.MainRepositoryImpl
import com.example.data.RetrofitService
import com.example.data.type_adapters.HabitAdapter
import com.example.domain.MainUseCase
import com.example.domain.MainUseCaseImpl
import com.example.domain.entities.Habit
import com.example.habitstracker.MainActivity
import com.google.gson.GsonBuilder
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.internal.DaggerGenerated
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ProviderModule {

    @Provides
    fun provideMainUseCase(mainRepository: MainRepositoryImpl): MainUseCase {
        return MainUseCaseImpl(mainRepository)
    }

    @Provides
    fun provideMainRepository(retrofitService: RetrofitService): MainRepositoryImpl {
        return MainRepositoryImpl(retrofitService)
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
}

@Singleton
@Component(modules= [ProviderModule::class])
interface ApplicationComponent {

    fun inject(): MainUseCase
}

class MyApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.create()
    }
}