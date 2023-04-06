## LifeCycle Aware Components

* Most of the code is written inside Activity LifeCycle Methods - onCreate, onResume,
onPause etc. Due to this, Activity has multiple responsibilities.
* But there are scenarios where we want to take actions based on the activity lifecyle.
* For e.g.
	= Access the User's Location.
	= Playing Video.
	= Downloading Images.


It mainly consists of two Components :-
	* LifeCycle Owner
	* LifeCycle Observer 