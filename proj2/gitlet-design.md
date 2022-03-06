# Gitlet Design Document

**Name**: procrastin

## Classes and Data Structures

### MAIN
Takes and verifies user input
#### Fields
1. None?

### COMMIT
Creates a commit object with metadata + hashmap containing files tracked by that commit
#### Fields
1. message: String
2. timestamp: String 
3. parent ID: String
4. isMergeCommit: boolean
5. filesInCommit: TreeMap<filename, blob id>

### COMMIT TREE
#### Fields
1. commitHistory: TreeMap<hashcode, Commit object>

### REPOSITORY
#### Fields
1. CWD: directory
2. gitlet repository: directory
3. HEAD: file containing path to current commit
4. refs: subdirectory containing branch files (each file contains current commit id)

### BLOB
Creates a Blob object from file content and gets its hashcode
#### Fields
1. filename
2. hashcode
3. contents

### STAGE
#### Fields
1. additionStage: HashMap<filename, hashcode>: staging area for addition
2. removalStage: HashMap<filename, hashcode>: staging area for removal
3. modifiedTrackedFiles: HashMap<filename, hashcode>: tracked files that were modified but not staged for commit
4. trackedFiles: HashMap<filename, hashcode>: all files that have ever been committed
5. untrackedFiles: HashMap<filename, hashcode>: filed in CWD that aren't staged or committed
6. index: serialized file containing all info about the staging area






## Algorithms


### MAIN
1. switch/case blocks: call appropriate methods
2. validateNumArguments(): check for number of args (see capers.Main)

### COMMIT
1. getters (get parent ID, date, message, filesInCommit...)
2. saveCommit(): creates new persistent file of commit object with hash code as name (similar to saveDog() in capers)
3. returnCommit(): returns deserialized commit object 
4. getHash(): returns hash code of any commit object
5. equals(): compares two commits (by their id)
6. getContent(): returns the blob id of a commit given its filename 

### COMMIT TREE
1. find(): prints out ids of all commits with given message 
2. log(): prints commit info history
3. globalLog()
4. checkout() [first checkout]
5. checkout() [second checkout]
6. reset()
7. merge(): merges files from given branch to current branch
8. findSplit(): returns the commit id of the split point (uses LCA algorithm)
9. createConflictFile(): returns a file that contains merge conflict results
10. caseMerge(): takes the split, HEAD and branch commits returns an int that indicates what merge situation we're in


### REPOSITORY
1. init(): creates new repo
2. commit(): calls saveCommit()
3. branch(): creates new branch file (same HEAD commit pointer)
4. switchBranch(): changes the HEAD file to point to the current branch
4. rm-branch(): deletes the branch with the given name
5. currentBranch(): returns the name of the current branch
6. checkout() [third checkout]


### BLOB
1. getHash(): returns hash code of any commit object
2. equals(): returns t/f

### STAGE
1. add()
2. rm()
3. status()
4. clearStagingArea()
5. removeFromStagingArea()
6. saveStage(): serializes the index file after it's been modified 
7. returnStage(): returns deserialized index file into a stage object

###UTILS [PROVIDED]
1. sha1(): returns hash code
2. restrictedDelete(): Deletes FILE if it exists and is not a directory
3. readContents(): returns contents of file as byte array
4. readContentsAsString(): returns entire contents of file as a String
5. writeContents(): writes the result of concatenating the bytes in CONTENTS to FILE
6. readObject(): returns an object of type T read from file
7. writeObject(): write OBJ to FILE
8. plainFilenamesIn(): returns a list of the names of all plain files in the directory DIR
9. join(): returns the concatenation of FIRST and OTHERS into a File designator
10. error(): returns a GitletException (error)
11. message(): prints a message composed of MSG and ARGS



## Persistence
1. index file: stores all info about the staging area: added/removed files, untracked files, untracked but unmodified files...
2. HEAD file: contains a path to the current branch 
3. branches folder: contains branch files with commit id's of each head 
4. commit files: each commit object will be serialized into a file named after the commit's id
5. commit tree: contains an id to commit mapping of the commit history
6. blob files:  stores the serialized content of files
