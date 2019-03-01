package com.jointdelivery.di


//import com.jointdelivery.loginmodule.activities.LoginActivity
import com.jointdelivery.fragments.homeFragments.*
import com.jointdelivery.appviewmodule.loginactivities.*
import com.jointdelivery.appviewmodule.mapactivities.YourRoutsMapActivity
import com.jointdelivery.appviewmodule.registeractivities.DriverInfoActivity
import com.jointdelivery.appviewmodule.registeractivities.SignUpActivityWithPhone
import com.jointdelivery.appviewmodule.registeractivities.VehicleDetailActivity
import com.jointdelivery.appviewmodule.registeractivities.YourInfoActivity
import com.jointdelivery.fragments.pagerFragments.VehicleInfoFragment
import com.jointdelivery.fragments.pagerFragments.YourInfoFragment
import dagger.Component

import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface AppComponent {
    fun inject(activity: SplashScreenActivity)
    fun inject(activity: SignUpActivityWithPhone)
    fun inject(activity: OtpScreenActivity)
    fun inject(activity: VehicleDetailActivity)
    fun inject(activity: YourInfoActivity)
    fun inject(activity: DriverInfoActivity)
    fun inject(activity: ForgetPasswordActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: ResetPasswordActivity)
    fun inject(activity: BackgroundProcessActivity)

    fun inject(fragment: AssignedFragment)
    fun inject(fragment: CompletedFragment)
    fun inject(fragment: MessagesFragmenrt)
    fun inject(fragment: YourRoutsMapActivity)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NotificationFragment)
     fun inject(fragment: ProfileFragment)
     fun inject(fragment: YourInfoFragment)
     fun inject(fragment: VehicleInfoFragment)

}
