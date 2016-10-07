Make sure you have all the source code completely.
Here's the list of the files you'll need:
1. Scanner.lex
2. Parser.cup
3. Context.java
4. Hash.java
5. Bucket.java
6. Generate.java
7. HMachine.java
8. Machine.java

Also, make sure you have the libraries needed and have set
the classpath. (JLex & java_cup)
You'll need it to compile the first two source codes.

To run the program properly, compile all the source codes.
If you have no idea how to do it, type in every single step
of the following:

1. If you have the 'compile.bat' file and are using 
   Windows/MS-DOS
> compile

2. If you're using Windows/MS-DOS
> java JLex.Main Scanner.lex
> java java_cup.Main Parser.cup
> ren Scanner.lex.java Yylex.java
> javac *.java

3. If you're using Linux
> java JLex.Main Scanner.lex
> java java_cup.Main Parser.cup
> mv Scanner.lex.java Yylex.java
> javac *.java

Now, you're ready to give the compiler a try.
To use it, type in:
> java parser < [input_file]

If there's no error occured in the compilation,
you'll see the target code printed.
Else you'll get error messages.

You would like to save the target code to a file,
once you're sure it's error-free. Then, you can
try to execute the target code.
To save and execute the target code, type in:
> java parser < [input_file] > [target_file]
> java Machine [target_file]

Well, that's all about it. It's up to you now.
Have fun!