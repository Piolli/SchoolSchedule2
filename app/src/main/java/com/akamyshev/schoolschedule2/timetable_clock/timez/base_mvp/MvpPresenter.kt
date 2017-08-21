package com.akamyshev.usemvp

/**
 * Created by alexandr on 10.08.17.
 */
interface MvpPresenter<in V : MvpView> {
    fun bindView(view: V)
    fun unbindView()
    fun destroy()
}