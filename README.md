# Reproduce Native Image Build Issue Described @ https://github.com/quarkusio/quarkus/discussions/31696

## Hardware

OS:         Ubuntu 22.04.2 LTS

Processor:  AMD® Ryzen 7 3800x 8-core processor × 16

RAM:        32 GiB

## Start local, insecure registry in docker (rootless mode).

```
$ scripts/start-local-registry.sh
```

## Jar Image Build Works

```
./gradlew clean build -Dquarkus.profile=k8s-jib-jar  -Dquarkus.container-image.push=true
```

## Attempt Native Image Build

```
$ ./gradlew clean build -Dquarkus.profile=k8s-jib-native  -Dquarkus.container-image.push=true
```

## Linker Command `ld` Fails:

    Linker command output:
    Using built-in specs.
    COLLECT_GCC=/usr/bin/gcc
    COLLECT_LTO_WRAPPER=/usr/libexec/gcc/x86_64-redhat-linux/8/lto-wrapper
    OFFLOAD_TARGET_NAMES=nvptx-none
    OFFLOAD_TARGET_DEFAULT=1
    Target: x86_64-redhat-linux
    Configured with: ../configure --enable-bootstrap --enable-languages=c,c++,fortran,lto --prefix=/usr --mandir=/usr/share/man --infodir=/usr/share/info --with-bugurl=http://bugzilla.redhat.com/bugzilla --enable-shared --enable-threads=posix --enable-checking=release --enable-multilib --with-system-zlib --enable-__cxa_atexit --disable-libunwind-exceptions --enable-gnu-unique-object --enable-linker-build-id --with-gcc-major-version-only --with-linker-hash-style=gnu --enable-plugin --enable-initfini-array --with-isl --disable-libmpx --enable-offload-targets=nvptx-none --without-cuda-driver --enable-gnu-indirect-function --enable-cet --with-tune=generic --with-arch_32=x86-64 --build=x86_64-redhat-linux
    Thread model: posix
    gcc version 8.5.0 20210514 (Red Hat 8.5.0-18) (GCC)
    COMPILER_PATH=/usr/libexec/gcc/x86_64-redhat-linux/8/:/usr/libexec/gcc/x86_64-redhat-linux/8/:/usr/libexec/gcc/x86_64-redhat-linux/:/usr/lib/gcc/x86_64-redhat-linux/8/:/usr/lib/gcc/x86_64-redhat-linux/
    LIBRARY_PATH=/usr/lib/gcc/x86_64-redhat-linux/8/:/usr/lib/gcc/x86_64-redhat-linux/8/../../../../lib64/:/lib/../lib64/:/usr/lib/../lib64/:/usr/lib/gcc/x86_64-redhat-linux/8/../../../:/lib/:/usr/lib/
    COLLECT_GCC_OPTIONS='-z' 'noexecstack' '-o' '/project/discussion-31696-repro-0.0.1-SNAPSHOT-runner' '-v' '-L/tmp/SVM-10820196245215039312' '-L/opt/mandrel/lib/static/linux-amd64/glibc' '-L/opt/mandrel/lib/svm/clibraries/linux-amd64' '-mtune=generic' '-march=x86-64'
    /usr/libexec/gcc/x86_64-redhat-linux/8/collect2 -plugin /usr/libexec/gcc/x86_64-redhat-linux/8/liblto_plugin.so -plugin-opt=/usr/libexec/gcc/x86_64-redhat-linux/8/lto-wrapper -plugin-opt=-fresolution=/tmp/ccBsh1Ur.res -plugin-opt=-pass-through=-lgcc -plugin-opt=-pass-through=-lgcc_s -plugin-opt=-pass-through=-lc -plugin-opt=-pass-through=-lgcc -plugin-opt=-pass-through=-lgcc_s --build-id --no-add-needed --eh-frame-hdr --hash-style=gnu -m elf_x86_64 -dynamic-linker /lib64/ld-linux-x86-64.so.2 -o /project/discussion-31696-repro-0.0.1-SNAPSHOT-runner -z noexecstack /usr/lib/gcc/x86_64-redhat-linux/8/../../../../lib64/crt1.o /usr/lib/gcc/x86_64-redhat-linux/8/../../../../lib64/crti.o /usr/lib/gcc/x86_64-redhat-linux/8/crtbegin.o -L/tmp/SVM-10820196245215039312 -L/opt/mandrel/lib/static/linux-amd64/glibc -L/opt/mandrel/lib/svm/clibraries/linux-amd64 -L/usr/lib/gcc/x86_64-redhat-linux/8 -L/usr/lib/gcc/x86_64-redhat-linux/8/../../../../lib64 -L/lib/../lib64 -L/usr/lib/../lib64 -L/usr/lib/gcc/x86_64-redhat-linux/8/../../.. --gc-sections --dynamic-list /tmp/SVM-10820196245215039312/exported_symbols.list --exclude-libs ALL -x discussion-31696-repro-0.0.1-SNAPSHOT-runner.o /opt/mandrel/lib/svm/clibraries/linux-amd64/liblibchelper.a /opt/mandrel/lib/static/linux-amd64/glibc/libnet.a /opt/mandrel/lib/static/linux-amd64/glibc/libextnet.a /opt/mandrel/lib/static/linux-amd64/glibc/libnio.a /opt/mandrel/lib/static/linux-amd64/glibc/libmanagement_ext.a /opt/mandrel/lib/static/linux-amd64/glibc/libjava.a /opt/mandrel/lib/static/linux-amd64/glibc/libfdlibm.a /opt/mandrel/lib/static/linux-amd64/glibc/libzip.a /opt/mandrel/lib/svm/clibraries/linux-amd64/libjvm.a -lz -lpthread -ldl -lrt -lgcc --as-needed -lgcc_s --no-as-needed -lc -lgcc --as-needed -lgcc_s --no-as-needed /usr/lib/gcc/x86_64-redhat-linux/8/crtend.o /usr/lib/gcc/x86_64-redhat-linux/8/../../../../lib64/crtn.o
    /usr/bin/ld: cannot open output file /project/discussion-31696-repro-0.0.1-SNAPSHOT-runner: Permission denied
    collect2: error: ld returned 1 exit status
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.image.NativeImageViaCC.handleLinkerFailure(NativeImageViaCC.java:203)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.image.NativeImageViaCC.runLinkerCommand(NativeImageViaCC.java:151)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.image.NativeImageViaCC.write(NativeImageViaCC.java:117)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.doRun(NativeImageGenerator.java:718)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGenerator.run(NativeImageGenerator.java:535)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.buildImage(NativeImageGeneratorRunner.java:403)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.build(NativeImageGeneratorRunner.java:580)
    at org.graalvm.nativeimage.builder/com.oracle.svm.hosted.NativeImageGeneratorRunner.main(NativeImageGeneratorRunner.java:128)
    1.5s (3.2% of total time) in 30 GCs | Peak RSS: 6.49GB | CPU load: 10.15
    ========================================================================================================================
    Failed generating 'discussion-31696-repro-0.0.1-SNAPSHOT-runner' after 48.0s.
    Error: Image build request failed with exit status 1
    > Task :quarkusAppPartsBuild FAILED
    
    FAILURE: Build failed with an exception.
    
    * What went wrong:
      Execution failed for task ':quarkusAppPartsBuild'.
    > There was a failure while executing work items
    > A failure occurred while executing io.quarkus.gradle.tasks.worker.BuildWorker
    > io.quarkus.builder.BuildException: Build failure: Build failed due to errors
    [error]: Build step io.quarkus.deployment.pkg.steps.NativeImageBuildStep#build threw an exception: io.quarkus.deployment.pkg.steps.NativeImageBuildStep$ImageGenerationFailureException: Image generation failed. Exit code: 1
    at io.quarkus.deployment.pkg.steps.NativeImageBuildStep.imageGenerationFailed(NativeImageBuildStep.java:458)
    at io.quarkus.deployment.pkg.steps.NativeImageBuildStep.build(NativeImageBuildStep.java:264)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:568)
    at io.quarkus.deployment.ExtensionLoader$3.execute(ExtensionLoader.java:909)
    at io.quarkus.builder.BuildContext.run(BuildContext.java:282)
    at org.jboss.threads.ContextHandler$1.runWith(ContextHandler.java:18)
    at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2513)
    at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1538)
    at java.base/java.lang.Thread.run(Thread.java:833)
    at org.jboss.threads.JBossThread.run(JBossThread.java:501)
    
    
    * Try:
    > Run with --stacktrace option to get the stack trace.
    > Run with --info or --debug option to get more log output.
    > Run with --scan to get full insights.
    > Get more help at https://help.gradle.org.
    
    Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.
    
    You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.
    
    For more on this, please refer to https://docs.gradle.org/8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.
    
    BUILD FAILED in 1m 28s
    8 actionable tasks: 8 executed
