package com.guava.prototype.bridge.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.1)",
    comments = "Source: chat_bridge.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class LlmBridgeServiceGrpc {

  private LlmBridgeServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "guava.prototype.bridge.v1.LlmBridgeService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.PromptRequest,
      com.guava.prototype.bridge.proto.ModelEvent> getStreamReplyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamReply",
      requestType = com.guava.prototype.bridge.proto.PromptRequest.class,
      responseType = com.guava.prototype.bridge.proto.ModelEvent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.PromptRequest,
      com.guava.prototype.bridge.proto.ModelEvent> getStreamReplyMethod() {
    io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.PromptRequest, com.guava.prototype.bridge.proto.ModelEvent> getStreamReplyMethod;
    if ((getStreamReplyMethod = LlmBridgeServiceGrpc.getStreamReplyMethod) == null) {
      synchronized (LlmBridgeServiceGrpc.class) {
        if ((getStreamReplyMethod = LlmBridgeServiceGrpc.getStreamReplyMethod) == null) {
          LlmBridgeServiceGrpc.getStreamReplyMethod = getStreamReplyMethod =
              io.grpc.MethodDescriptor.<com.guava.prototype.bridge.proto.PromptRequest, com.guava.prototype.bridge.proto.ModelEvent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamReply"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.guava.prototype.bridge.proto.PromptRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.guava.prototype.bridge.proto.ModelEvent.getDefaultInstance()))
              .setSchemaDescriptor(new LlmBridgeServiceMethodDescriptorSupplier("StreamReply"))
              .build();
        }
      }
    }
    return getStreamReplyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.ClientEvent,
      com.guava.prototype.bridge.proto.ModelEvent> getChatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Chat",
      requestType = com.guava.prototype.bridge.proto.ClientEvent.class,
      responseType = com.guava.prototype.bridge.proto.ModelEvent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.ClientEvent,
      com.guava.prototype.bridge.proto.ModelEvent> getChatMethod() {
    io.grpc.MethodDescriptor<com.guava.prototype.bridge.proto.ClientEvent, com.guava.prototype.bridge.proto.ModelEvent> getChatMethod;
    if ((getChatMethod = LlmBridgeServiceGrpc.getChatMethod) == null) {
      synchronized (LlmBridgeServiceGrpc.class) {
        if ((getChatMethod = LlmBridgeServiceGrpc.getChatMethod) == null) {
          LlmBridgeServiceGrpc.getChatMethod = getChatMethod =
              io.grpc.MethodDescriptor.<com.guava.prototype.bridge.proto.ClientEvent, com.guava.prototype.bridge.proto.ModelEvent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Chat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.guava.prototype.bridge.proto.ClientEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.guava.prototype.bridge.proto.ModelEvent.getDefaultInstance()))
              .setSchemaDescriptor(new LlmBridgeServiceMethodDescriptorSupplier("Chat"))
              .build();
        }
      }
    }
    return getChatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LlmBridgeServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceStub>() {
        @java.lang.Override
        public LlmBridgeServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new LlmBridgeServiceStub(channel, callOptions);
        }
      };
    return LlmBridgeServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LlmBridgeServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceBlockingStub>() {
        @java.lang.Override
        public LlmBridgeServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new LlmBridgeServiceBlockingStub(channel, callOptions);
        }
      };
    return LlmBridgeServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LlmBridgeServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<LlmBridgeServiceFutureStub>() {
        @java.lang.Override
        public LlmBridgeServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new LlmBridgeServiceFutureStub(channel, callOptions);
        }
      };
    return LlmBridgeServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void streamReply(com.guava.prototype.bridge.proto.PromptRequest request,
        io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStreamReplyMethod(), responseObserver);
    }

    /**
     */
    default io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ClientEvent> chat(
        io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getChatMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service LlmBridgeService.
   */
  public static abstract class LlmBridgeServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return LlmBridgeServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service LlmBridgeService.
   */
  public static final class LlmBridgeServiceStub
      extends io.grpc.stub.AbstractAsyncStub<LlmBridgeServiceStub> {
    private LlmBridgeServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LlmBridgeServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new LlmBridgeServiceStub(channel, callOptions);
    }

    /**
     */
    public void streamReply(com.guava.prototype.bridge.proto.PromptRequest request,
        io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getStreamReplyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ClientEvent> chat(
        io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getChatMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service LlmBridgeService.
   */
  public static final class LlmBridgeServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<LlmBridgeServiceBlockingStub> {
    private LlmBridgeServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LlmBridgeServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new LlmBridgeServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.guava.prototype.bridge.proto.ModelEvent> streamReply(
        com.guava.prototype.bridge.proto.PromptRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getStreamReplyMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service LlmBridgeService.
   */
  public static final class LlmBridgeServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<LlmBridgeServiceFutureStub> {
    private LlmBridgeServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LlmBridgeServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new LlmBridgeServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_STREAM_REPLY = 0;
  private static final int METHODID_CHAT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_REPLY:
          serviceImpl.streamReply((com.guava.prototype.bridge.proto.PromptRequest) request,
              (io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHAT:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.chat(
              (io.grpc.stub.StreamObserver<com.guava.prototype.bridge.proto.ModelEvent>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getStreamReplyMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.guava.prototype.bridge.proto.PromptRequest,
              com.guava.prototype.bridge.proto.ModelEvent>(
                service, METHODID_STREAM_REPLY)))
        .addMethod(
          getChatMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.guava.prototype.bridge.proto.ClientEvent,
              com.guava.prototype.bridge.proto.ModelEvent>(
                service, METHODID_CHAT)))
        .build();
  }

  private static abstract class LlmBridgeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LlmBridgeServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.guava.prototype.bridge.proto.ChatBridgeProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LlmBridgeService");
    }
  }

  private static final class LlmBridgeServiceFileDescriptorSupplier
      extends LlmBridgeServiceBaseDescriptorSupplier {
    LlmBridgeServiceFileDescriptorSupplier() {}
  }

  private static final class LlmBridgeServiceMethodDescriptorSupplier
      extends LlmBridgeServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    LlmBridgeServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (LlmBridgeServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LlmBridgeServiceFileDescriptorSupplier())
              .addMethod(getStreamReplyMethod())
              .addMethod(getChatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
