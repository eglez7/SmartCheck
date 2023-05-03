package app.smartdevelop.smartcheck

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun Context.toast(texto: String, lenght:Int= Toast.LENGTH_SHORT) {
    Toast.makeText(this, texto, lenght).show()
}

fun RecyclerView.ViewHolder.toast(texto: String, lenght:Int= Toast.LENGTH_SHORT){
    itemView.context.toast(texto, lenght)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater
        .from(context)
        .inflate(layoutRes, this, false)
}

// PERMITE LLAMAR A UNA ACTIVIDAD DESDE OTRA
inline fun <reified T: Activity> Context.startActivity(vararg pairs: Pair<String, Any?>){
    Intent(this,T::class.java).apply {
        putExtras(bundleOf(*pairs))
    }
        .also (::startActivity)
}

inline fun <reified T: Fragment> Context.startFragment(vararg pairs: Pair<String, Any?>){
    Intent(this,T::class.java).apply {
        putExtras(bundleOf(*pairs))
    }
        .also (::startActivity)
}