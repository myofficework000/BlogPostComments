package com.abhishekpathak.posts.commons

import com.abhishekpathak.posts.core.application.CoreApp
import com.abhishekpathak.posts.details.di.DaggerDetailsComponent
import com.abhishekpathak.posts.details.di.DetailsComponent
import com.abhishekpathak.posts.list.di.DaggerListComponent
import com.abhishekpathak.posts.list.di.ListComponent
import javax.inject.Singleton

@Singleton
object PostDH {
    private var listComponent: ListComponent? = null
    private var detailsComponent: DetailsComponent? = null

    fun listComponent(): ListComponent {
        if (listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

    fun detailsComponent(): DetailsComponent {
        if (detailsComponent == null)
            detailsComponent = DaggerDetailsComponent.builder().listComponent(listComponent()).build()
        return detailsComponent as DetailsComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }
}