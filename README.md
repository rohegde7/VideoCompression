# VideoCompression
Assignment given during interview process of TRELL

### Features:
* Compressing video files in our local storage system with desired bitrate

### Tech and Architecture:
* We have used __Kotlin__ as the primary programming language
* This application takes advantage of __MVVM architecture__
* __Activity, ViewModel, Repository, LiveData, ObservableFields, DataBinding__ are the basic building blocks of this architecture
* All the UI related work like showing Toast, starting a new activity, showing the progress bar, etc is done in the Activity. 
* Business logic is written in the ViewModel. Also the observable fields are present in the ViewModel which are observed from the XML when we use data binding. 
* All API calls, DB calls, File operation and major calculation or operation happen from within the repository.
* It's a 1 way reference flow from Activity -> View Model -> Repository. For ViewModel/Repository to communicate back in the chain we use live data which is observed by the concerned entity.
* Constraint Layout has been used to design the UI

### Libraries:
* __nl.bravobit:android-ffmpeg:1.1.7__ library is used for video compression
* __Glide__ is used for loading thumbnail of the videos

### App screenshots:
![Image description](https://github.com/rohegde7/VideoCompression/blob/master/app%20screenshorts/list_of_videos.jpg)
![Image description](https://github.com/rohegde7/VideoCompression/blob/master/app%20screenshorts/sectecting_bitrate.jpg)
![Image description](https://github.com/rohegde7/VideoCompression/blob/master/app%20screenshorts/converting_videojpg.jpg)
