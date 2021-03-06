# ChatHammer 9000

## Building and running the project

We use an ant-based NetBeans project.

An overview of the used commands:

    ant clean		Clean the project
    ant compile		Build the project
    ant run			Run the main class
    ant jar			Build the application jar
    ant plugins		Build the plugin jars

For added convenience there also exists a run-class and a run-test script.

## Coding style

In any project where more than one programmer is involved, there needs to be
some kind of convention around code style. Since we are programming in the
Java language, it makes sense to use the Sun code conventions[1], since they
are the most commonly used.

[1]: http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html

## Git style

In addition, let's have a few conventions for git usage.

### Commit messages

Use descriptive commit messages. The git convention is to use:

> Short (50 chars or less) summary of changes.
>
> After an empty line follows a more detailed explanation. This
> can be as long as you want -- and it's optional if you think
> the short summary is descriptive enough.
    
As you might have noticed from reading this document, everything will be 
documented and commented in english.

### Use rebase

Use `git pull --rebase` for a cleaner git history. You can make this the
default by adding `rebase = true` to your `.git/config`, ie.

    [branch "master"]
        remote = origin
        merge = refs/heads/master
        rebase = true

If this is set, `git pull` will automatically rebase.

### Branches

Since we're all more or less working on separate packages, we all can work on
the master branch for now. However, if you want to do really crazy shit (e.g.
writing a plugin in Clojure), please use a separate branch until it's stable
and approved.
