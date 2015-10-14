Developers love bug reports. It means you found something wrong, and want to help make the mod better. That said, an incomplete or imprecise bug report can be incredibly frustrating. Here's some general guidelines to help you write clear, concise, and useful bug reports. 

Please read through these before reporting a new bug on the [issue tracker](https://github.com/jaredlll08/ModTweaker2/issues/new).
Also check if there is already a existing issue which look similar like your one.

## Is it a bug?
The first step in submitting a good bug report is making sure it's actually a bug with ModTweaker2. This seems like a no-brainer, but there's a lot of bugs that get reported which have very little to do with ModTweaker2, or have more to do with someone's particular play style. Check the following to help make sure you don't report invalid bugs:
* Can you reproduce it? A bug that can't be reproduced, can't be fixed.
  * If you can reproduce it, you've got to tell us the steps to reproduce it. Knowing that there is a bug out there that we can't fix because we can't find it is really frustrating for us.
* Can it be reproduced with *just* ModTweaker2 (or ModTweaker2 and the relevant mods) installed? 
  * Frequently, mods will conflict with each other in one way or another. Knowing specifically what mods are involved goes a long way towards helping us fix a bug.
  * If you're running a modpack, please tell us which one, and what version. Saying "I'm running that one FTB modpack" doesn't help me. Saying "I'm running Resonant Rise 2.9.2.3 with no custom options" is extremely helpful.
    * If you're running a custom modpack with more than three or four mods, you **must** identify which mods are causing the issue. If you don't identify the cause, it's nearly impossible for us to fix.
    * Linking to a long list of all the mods in your pack isn't helpful. Figure out what mods cause it (or at least narrow it down to less than 10 mods), and report that.
* Is it actually something broken, or does it have more to do with your playstyle or another mod that behaves weirdly?
  * If you have an idea for making the mod "better", I'm all ears...but we may disagree, and reserve the right to say "no" to any feature request.
  * We can't promise 100% compatibility with all mods. Some stuff is just gonna break. That said, I'll do my best to make sure that game-breaking stuff gets patched somehow.
* If the bug has to do with another mod, removing all unnecessary mods ensures that there's no conflict.

## Are you running the latest version?
Normally, we don't recommend using dev builds, but when you run into a bug, please check to see if it's still a problem in the latest released dev build. You could also try using the [latest build from Jenkins](http://ci.tterrag.com/job/ModTweaker2/), but those tend to be unstable at times. Making sure that any dependencies are up to date is a good idea too. Please do not use Jenkin builds in a modpack because of obvious reasons. If you find a bug with the Jenkins version please add that to your bug report.

## Basic requirements
Once you know it's a bug, there's a couple of things that we absolutely must have. Reports without these will normally not get fixed, because there simply isn't enough information.
* Steps to reproduce. If We can't reproduce it, We can't fix it. We say this more than once because it's one of the more important bits.
  * Steps to reproduce should be specific. 
  * If the bug involves multiple steps, be specific with all of them.
  * Here's some examples:
    * "When I right click on block Y, my client crashes."
    * "Using item X from someOtherMod (version 1.2.3.4) to pick up block Y makes it lose all items from the inventory"
    * "If I place block X next to block Y when sneaking, all nearby sand turns into lava"
* What version of Modtweaker, the Mod which handler you are using and Minetweaker are you running?
* Your ZenScripts. If your are able to narrow down the statements which causes problems, then that chunk, otherwise all Scripts. (For most issues the Minetweaker prints the the file and line which cause it.)
* The MineTweaker log, where all actions it does are logged. (minetweaker.log in your Minecraft directory)
* Was there a crash? If so, we need the (vanilla) crashlog and your Forge log.
  * Enable debugMode in the config. This prints extra information to the logs, and helps us figure out what is going on.
  * Use [Gist](https://gist.github.com/), [Pastebin](http://pastebin.com/) or some other text paste site for your crashlog/Forge log or any config files (don't use screenshots for text files please!). 
  * Pasting the full text into a ticket can make it hard to read.
  * If you're using a non-vanilla launcher, (eg, MultiMC or the FTB launcher) we still need the **vanilla crash log**. If we need more info, I'll ask.   
    * Getting [crashlogs from MultiMC](https://github.com/MultiMC/MultiMC5/wiki/Providing-crash-reports). 
* One bug per report. If you report multiple problems on a single issue report, it's often easy to get them mixed up or have partial fixes.
* Name it descriptively. An issue called "BROKEN!!!11" or "lots of buggggsss" isn't helpful. One named "Error with Forestry 4.03 and latest repo build of MT2 - NoClassDefFoundError" or "[Suggestion] Iguana's Tinker Tweaks - XP" is much better.

## Helpful additions
Adding this stuff can make my job a lot easier. It's not required, but better bug reports means a faster response from me.
* Use Github-flavored markdown! If you have short ZenScript code, that you want to write, use this code: 
  `recipes.remove(<minecraft:log:1>)`

## Feedback
Often times, I'll have questions about the problem you report. Keep an eye on your bug report, because if we have a question, and you don't reply for a month, the bug may not get fixed until after you reply.

## Contact
Questions, comments, confusion, need to check if something is how it's supposed to be? we hang out in the Esper.net #jaredlll08 channel, and you're welcome to drop by and chat.

## Patience
We are mod developers in our spare time, and we do my best to respond immediately when bugs get reported. That said, we have a job and a life outside of Minecraft. Any bug that gets reported will get fixed eventually, as long as enough information is included.

This was shameless copied from [Minechems Guidelines](https://github.com/jakimfett/Minechem/wiki/Bug-Reporting-Guidelines). Thank you @jakimfett
