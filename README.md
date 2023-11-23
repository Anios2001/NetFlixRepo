# NetFlixRepo
NetFlix Project with Nullclass
## Problem Statement 
The problem was of circular Subscription and Account Management employed by Netflix.
One of friends used to buy a subscription with the community money we gathered and then we used to use the account on rolling basis. The concurrent connections were 10 and we were a group of 20 people wanting to watch 20 different movies in the same 3 hour off time we got from our studies. 
### The following plausible solutions were not optimal 
1. Just change the time.  We had limited time of our studies.
2. Watch some movies together. Not Possible we had a Tech Marvel Fan to a Magic Harry Potter to dashing DC Fan.
3. Do something else, really we are problem solvers we do everything with code.


So, me and my 5 nerd friends decided we want our own service hosted on using combined hosting capacity of our mobile phones to do that. We got to the drwaing board and came up with a distributed CDN for mobiles with minimal throughput loss and whose capacity to manage the throughput increased with addition of new users.
```Design a system capable of streaming HD Quality content from a central server using the hosting capability of the connected nodes.```  

## Detailed Analysis 
The design of such a system as defined many times involves 5 steps : 
1. Ingestion : The source of the video i.e., video files and live event recording.
2. Transcoding : The process if converting one kind of video file into another is called transcoding. An mpeg transcoded to mp4, wav or ffs. 
3. Management : Handlling the business side of the movie streaming i.e., ad insertion and security while streaming so that content is securely delivered to end users 
4. Delivery and Playback: When final stream is ready with ads, then it is transmitted over the networks to reach the end user and played there. The process is complex as the devices support is ever growing and watching a movie anywhere is the market experience. To make the system inclusive of all formats the designers had to bundle 20 different kind of streams of a single video and that is converted into the suitable resolution needed at the end device. This becomes the playback concern. 
5. Chaos Engineering: Testing the system is a big problem as you would not know how the people are going to use the streaming service therefore, you have to prepare for the worst possible consequences and that can be done by Chaos Monkey tool which generates requests for the server that aims to break it in order to judge the effectiveness and resiliancy of the system.
