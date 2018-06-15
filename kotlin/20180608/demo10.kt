
enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

data class SiteVisit(
	val path:String,
	val duration:Double,
	val os: OS
)

val log = listOf(
	SiteVisit("/",23.0,OS.WINDOWS),
	SiteVisit("/",22.0,OS.MAC),
	SiteVisit("/login",12.0,OS.WINDOWS),
	SiteVisit("/signup",8.0,OS.IOS),
	SiteVisit("/",16.3,OS.ANDROID)
)

fun main(args:Array<String>)
{
	val averageWindowsDuration = log.filter{ it.os == OS.WINDOWS }
		.map(SiteVisit::duration)
		.average()

	println(averageWindowsDuration)
}
