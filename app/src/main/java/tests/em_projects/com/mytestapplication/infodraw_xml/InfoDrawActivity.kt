package tests.em_projects.com.mytestapplication.infodraw_xml

// import android.media.MediaPlayer
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import org.w3c.dom.Attr
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import tests.em_projects.com.mytestapplication.R
import java.io.InputStream


// Ref: https://github.com/gipsh/vlc-android-rtsp-test
// Ref: https://stackoverflow.com/questions/36952741/rtsp-live-video-streaming

class InfoDrawActivity : AppCompatActivity() {
    private val TAG = "InfoDrawActivity"

    private val CAPTURE_DEVICES = "mrs:CaptureDevices"
    private val CAPTURE_DEVICE = "mrs:CaptureDevice"
    private val DEVICE_ID_ATTR = "mrs:DeviceID"
    private val VIDEO_CHANNELS = "mrs:VideoChannels"
    private val VIDEO_CHANNEL = "mrs:VideoChannel"
    private val VIDEO_INDEX = "mrs:Index"

    private lateinit var parser: XMLParser
    private var doc: Document? = null

    // UI Components
    private lateinit var videoView: VideoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_draw)
        Log.d(TAG, "onCreate")

        videoView = findViewById(R.id.videoView) as VideoView

        val cameraData: CameraData? = infoDrawCapture()

        if (cameraData != null) {
            openRtspStreaming(cameraData)
        }

    }


    private fun openRtspStreaming(cameraData: CameraData) {
        val videoUrl = "rtsp://mm.infodraw.com:12654/stream/device_${cameraData.deviceId}_camera_1.sdp"
        val mediaController = MediaController(this)

        mediaController.setAnchorView(videoView)
        val uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)
        videoView.setMediaController(mediaController)
        videoView.requestFocus()
        videoView.start()

        videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {

            override fun onPrepared(mp: MediaPlayer?) {
                mp?.start()
                mp?.setOnVideoSizeChangedListener(object : MediaPlayer.OnVideoSizeChangedListener {

                    override fun onVideoSizeChanged(mp: MediaPlayer?, width: Int, height: Int) {
                        mp?.start()
                    }
                })
            }
        })
    }

    private fun infoDrawCapture(): CameraData? {

        // Loading XML
        val xmlStr: String? = loadXmlFromRaw(R.raw.infodraw_capture)

        if (xmlStr.toString().isNotEmpty()) {
            Log.d(TAG, "XML file loaded")

            // Create XML parser instance
            parser = XMLParser()

            // Convert xml to Document
            doc = parser.getDomElement(xmlStr.toString())

            val nodeList: NodeList? = doc?.getElementsByTagName(CAPTURE_DEVICES)
            Log.d(TAG, "Get Capture Devices list")

            // Get the first captured devices
            val captureDevices: Element? = nodeList?.item(0) as? Element
            Log.d(TAG, "Get Element: ${captureDevices?.tagName}")

            // All Capture Device from captureDevices
            val devicesList: NodeList? = captureDevices?.getElementsByTagName(CAPTURE_DEVICE)
            Log.d(TAG, "Get Capture devices children ${devicesList?.length}")

            // Get the first device
            val device: Element? = devicesList?.item(0) as? Element
            Log.d(TAG, "device is ${device?.tagName}")

            // Get the device id attribute
            val deviceIdAttr: Attr? = device?.getAttributeNode(DEVICE_ID_ATTR)
            Log.d(TAG, "device id attr ${deviceIdAttr?.name}")

            // Get device id value
            val deviceId = deviceIdAttr?.value
            Log.d(TAG, "Device Id is: $deviceId")

            // Get video channels list
            val videoNodeList: NodeList? = device?.getElementsByTagName(VIDEO_CHANNELS)
            Log.d(TAG, "Device has ${videoNodeList?.length} video channels")

            // Get the video channels element
            val videoChannels: Element? = videoNodeList?.item(0) as? Element
            Log.d(TAG, "video channel ${videoChannels?.tagName}")

            // Get the video channel
            val videoChannel: Element? = videoChannels?.getElementsByTagName(VIDEO_CHANNEL)?.item(0) as? Element
            Log.d(TAG, "Video channel ${videoChannel?.tagName}")

            // Get Video index element
            val videoIndex: Element? = videoChannel?.getElementsByTagName(VIDEO_INDEX)?.item(0) as? Element
            Log.d(TAG, "Video index ${videoIndex?.tagName}")

            // Get index value
            val index: String? = videoIndex?.firstChild?.nodeValue
            Log.d(TAG, "Index $index")

            if (deviceId == null || index == null) {
                return null
            }
            return CameraData(deviceId, index)

        } else {
            Log.d(TAG, "XML file failed to load")
            return null
        }

    }

    private fun loadXmlFromRaw(resId: Int): String? {
        val stream: InputStream = resources.openRawResource(resId)
        val buffer = ByteArray(stream.available())
        stream.read(buffer)
        stream.close()
        return String(buffer)
    }
}