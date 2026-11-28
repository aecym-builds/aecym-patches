.class public final Lapp/aecym/muse/AccountMenuSlot;
.super LX/DRv;
.source "AccountMenuSlot.java"

# interfaces
.implements LX/JDQ;


# direct methods
.method public constructor <init>()V
    .locals 1

    const/4 v0, 0x3

    invoke-direct {p0, v0}, LX/DRv;-><init>(I)V

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3

    check-cast p2, LX/2d4;

    # AuraAccountMenu invokes this content slot for the header row; a null content
    # makes it throw. Reuse the side panel's own title composable as the row.
    new-instance v1, Lcom/facebook/aura/conversation/view/HatchConversationHistorySidePanelKt$SidePanelTitle$content$1;

    const-string v2, "Switch account"

    invoke-direct {v1, v2}, Lcom/facebook/aura/conversation/view/HatchConversationHistorySidePanelKt$SidePanelTitle$content$1;-><init>(Ljava/lang/String;)V

    sget-object p1, LX/3C1;->A00:LX/3Cb;

    const/4 v0, 0x0

    invoke-static {v1, p1, p2, v0, v0}, Lcom/facebook/aura/auth/login/nativeflow/AuraAccountMenuKt;->AuraAccountMenu(LX/JDQ;LX/3C1;LX/2d4;II)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method
