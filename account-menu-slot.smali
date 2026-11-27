.class public final Lapp/aecym/muse/AccountMenuSlot;
.super Ljava/lang/Object;
.source "AccountMenuSlot.java"

# interfaces
.implements LX/JDQ;


# direct methods
.method public constructor <init>()V
    .locals 0

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    check-cast p2, LX/2d4;

    sget-object p1, LX/3C1;->A00:LX/3Cb;

    const/4 v0, 0x0

    invoke-static {v0, p1, p2, v0, v0}, Lcom/facebook/aura/auth/login/nativeflow/AuraAccountMenuKt;->AuraAccountMenu(LX/JDQ;LX/3C1;LX/2d4;II)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method
