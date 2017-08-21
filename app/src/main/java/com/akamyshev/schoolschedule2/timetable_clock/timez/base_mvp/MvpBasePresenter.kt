package com.akamyshev.usemvp

/**
 * Created by alexandr on 10.08.17.
 */
open class MvpBasePresenter<T : MvpView> : MvpPresenter<T> {
    protected var view: T? = null

    override fun bindView(view: T) {
        this.view = view
    }

    override fun unbindView() {
        view = null
    }

    override fun destroy() {

    }


}