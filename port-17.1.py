import re,sys
kt='build-source/muse-glass-navbar/patches/src/main/kotlin/app/aecym/muse/CustomColorNavBarPatch.kt'
s=open(kt).read()
def sub(old,new,count=1):
    global s
    n=s.count(old)
    assert n>=1 and (count==0 or n==count),(old,n)
    s=s.replace(old,new)
sub('"LX/0C0;"','"LX/07H;"')
sub('''    name = "A0K",
    returnType = "LX/9Tt;",
    parameters = listOf("LX/9N5;", "LX/9Tt;"),''','''    name = "A0D",
    returnType = "LX/3C1;",
    parameters = listOf("LX/2d4;", "LX/3C1;", "Lcom/facebook/aura/vds/compose/theme/AuraTheme;"),''')
sub('registerCount == 4','registerCount == 5')
sub('''        val alphaClass = classDefBy("LX/AH4;")
        check(alphaClass.methods.any { it.name == "A04" && it.returnType == "J" &&
            it.parameterTypes.map { p -> p.toString() } == listOf("J", "F")
        }) { "Expected packed-color alpha utility is missing." }''','''        val backgroundClass = classDefBy("LX/2pC;")
        check(backgroundClass.methods.any { it.name == "A03" && it.returnType == "LX/3C1;" &&
            it.parameterTypes.map { p -> p.toString() } == listOf("LX/3C1;", "LX/2bW;", "J")
        }) { "Expected background modifier utility is missing." }''')
sub('reference.name == "A0K" && reference.parameterTypes.map { it.toString() } == listOf("LX/9N5;", "LX/9Tt;")','reference.name == "A0D" && reference.parameterTypes.map { it.toString() } == listOf("LX/2d4;", "LX/3C1;", "Lcom/facebook/aura/vds/compose/theme/AuraTheme;")')
sub('call.registerCount == 2','call.registerCount == 3')
sub('Opcode.INVOKE_STATIC, 2, call.registerC, call.registerD, 0, 0, 0,','Opcode.INVOKE_STATIC, 3, call.registerC, call.registerD, call.registerE, 0, 0,')
sub('listOf("LX/9N5;", "LX/9Tt;"), "LX/9Tt;")','listOf("LX/2d4;", "LX/3C1;", "Lcom/facebook/aura/vds/compose/theme/AuraTheme;"), "LX/3C1;")')
sub('LX/9QZ;->A05(LX/9Tt;FFFF)LX/9Tt;','LX/2pj;->A0B(LX/3C1;FFFF)LX/3C1;')
sub('sget-object v0, LX/1Ov;->A00:LX/1Ov;','sget-object v0, LX/2bJ;->A00:LX/2bJ;')
sub('LX/1aV;->A02(LX/9Tt;LX/1pj;J)LX/9Tt;','LX/2pC;->A03(LX/3C1;LX/2bW;J)LX/3C1;')
sub('check-cast v0, LX/9Tt;','check-cast v0, LX/3C1;')
sub('"LX/0PB;" && r.name == "A00"','"LX/3FK;" && r.name == "A01"')
sub('"LX/95T;" to "A01", "LX/95T;" to "A03", "LX/95R;" to "A01",','"LX/2pJ;" to "A01", "LX/2pJ;" to "A03", "LX/2kc;" to "A00",')
sub('"LX/8v3;" to "AKc", "LX/1p2;" to "A9O",','"LX/2la;" to "AKf", "LX/2RG;" to "A9T",')
sub('"AFo",','"AFu",'); sub('"Adh",','"Adw",')
sub('classDefBy("LX/9Fi;")','classDefBy("LX/2kj;")')
sub('check-cast v$rowRegister, LX/9Tt;','check-cast v$rowRegister, LX/3C1;')
sub('"LX/8tZ;"','"LX/3GU;"')
sub('"LX/8t8;" to "A51", "LX/8lh;" to "A50", "LX/8u6;" to "A05",','"LX/32h;" to "A53", "LX/3Hm;" to "A52", "LX/3CH;" to "A04",')
sub('"LX/8u6;" to "A06", "LX/9OQ;" to "A04", "LX/8tM;" to "ALt",','"LX/3CH;" to "A05", "LX/3As;" to "A03", "LX/3Gx;" to "ALw",')
for v in ('17.0.0.10.160', '17.1.0.28.160'):
    if 'Tried on one device with Muse ' + v + '.' in s:
        s = s.replace('Tried on one device with Muse ' + v + '.', 'Ported from the device-tried 17.0.0.10.160 version, not yet device-tested on 17.1.0.28.160.')
        break
else:
    raise AssertionError('description not found')
s = s.replace('17.0.0.10.160"', '17.1.0.28.160"')
assert 'AppTarget(version = "17.1.0.28.160"' in s
open(kt,'w').write(s)
left=re.findall(r'LX/[0-9A-Za-z]+;',s)
print(sorted(set(left)))
j=open('MovingLens.java').read()
def js(old,new):
    global j
    assert j.count(old)>=1,old
    j=j.replace(old,new)
js('X.8v3','X.2la'); js('"AKc"','"AKf"'); js('X.95R").getMethod("A01"','X.2kc").getMethod("A00"')
js('X.9Fi','X.2kj'); js('X.1p2").getMethod("A9O"','X.2RG").getMethod("A9T"')
js('X.BkU','X.JDM'); js('X.9Tt','X.3C1'); js('X.95T','X.2pJ'); js('X.963','X.3Gj')
js('"AFo"','"AFu"'); js('"Adh"','"Adw"')
js('X.21K','X.2M4'); js('X.8lh','X.3Hm'); js('X.8xV','X.3ID'); js('X.I7L','X.2PB')
js('X.IBf").getMethod("A01"','X.2PI").getMethod("A04"')
js('"A50"','"A52"'); js('X.968','X.3BA'); js('X.8u6','X.3CH')
js('getMethod("A05");','getMethod("A04");'); js('getMethod("A06");','getMethod("A05");')
js('X.BkV','X.JDN'); js('X.8t8").getMethod("A51"','X.32h").getMethod("A53"')
js('X.9OQ").getMethod("A04",Class.forName("X.25c"))','X.3As").getMethod("A03",Class.forName("X.1K6"))')
js('X.8tM").getMethod("ALt")','X.3Gx").getMethod("ALw")')
open('MovingLens.java','w').write(j)
print(sorted(set(re.findall(r'X\.[0-9A-Za-z]{2,4}\b',j))))

r=open('README.md').read()
old="This implementation was tried on one user's phone."
# The README now covers more than the navbar patch, so only rewrite the old wording when present.
if '17.0.0.10.160' in r and old in r:
    r=r.replace('17.0.0.10.160','17.1.0.28.160')
    r=r.replace(old,"The earlier 17.0.0.10.160 version of this implementation was tried on one user's phone. This 17.1.0.28.160 port is statically checked against the app but not yet device-tested.")
    r=r.replace('Recordings show the rounded bar','Recordings of the earlier version show the rounded bar')
    r+='\nFor Muse 17.0.0.10.160 use the older bundle-7 release.\n'
    open('README.md','w').write(r)
