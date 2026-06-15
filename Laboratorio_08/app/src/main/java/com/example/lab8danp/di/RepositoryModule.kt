
package com.example.lab8danp.di

import com.example.lab8danp.data.repository.VideoCardRepositoryFakeImpl
import com.example.lab8danp.domain.repository.VideoCardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // @Binds es mucho más eficiente que @Provides cuando se enlazan interfaces con implementaciones
    @Binds
    abstract fun bindVideoCardRepository(
        fakeImpl: VideoCardRepositoryFakeImpl
    ): VideoCardRepository
}