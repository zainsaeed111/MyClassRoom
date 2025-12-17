# 🚀 Deploy to Render.com - Step by Step

## ✅ **100% FREE Forever** (No Credit Card Required)

---

## 📋 Prerequisites
- GitHub account
- Render account (free): https://render.com

---

## 🎯 **Super Easy Deployment Steps:**

### **Step 1: Push to GitHub**

If you haven't already:

```powershell
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Ready for Render deployment"

# Create a new repository on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/MyClassRoom.git
git push -u origin main
```

---

### **Step 2: Deploy on Render**

1. **Go to**: https://render.com
2. **Sign up/Login** with GitHub
3. **Click**: "New +" → "Web Service"
4. **Connect**: Your GitHub repository (MyClassRoom)
5. **Configure** (Render auto-fills most of this):

   **Basic Settings:**
   - **Name**: `myclassroom-api` (or any name you like)
   - **Region**: Choose closest to you
   - **Branch**: `main`
   - **Root Directory**: leave blank
   - **Runtime**: `Java`

   **Build & Deploy:**
   - **Build Command**: 
     ```
     ./gradlew installDist --no-daemon
     ```
   - **Start Command**: 
     ```
     ./build/install/MyClassRoom/bin/MyClassRoom
     ```

6. **Click**: "Create Web Service"

---

### **Step 3: Add PostgreSQL Database**

1. **In Render Dashboard**, click "New +" → "PostgreSQL"
2. **Configure**:
   - **Name**: `myclassroom-db`
   - **Database**: `classloom`
   - **User**: `classloom_user`
   - **Region**: Same as your web service
   - **Plan**: **Free**

3. **Click**: "Create Database"

4. **Copy Database Credentials**:
   - Go to your database dashboard
   - Copy these values:
     - **Internal Database URL** (starts with `postgresql://`)
     - **Username**
     - **Password**

---

### **Step 4: Set Environment Variables**

1. **Go to**: Your Web Service → "Environment" tab
2. **Add these variables**:

   | Key | Value |
   |-----|-------|
   | `DATABASE_URL` | Paste the Internal Database URL from Step 3 |
   | `DB_USER` | Paste the database username |
   | `DB_PASSWORD` | Paste the database password |
   | `PORT` | `10000` (Render's default) |
   | `JWT_SECRET` | `your-secret-key-here-make-it-strong` |
   | `JWT_DOMAIN` | `https://your-app-name.onrender.com/` |
   | `JWT_AUDIENCE` | `jwt-audience` |
   | `JWT_REALM` | `ktor sample app` |

3. **Click**: "Save Changes"

---

### **Step 5: Deploy!**

- Render automatically starts building and deploying
- **Wait 5-10 minutes** for first deployment
- You'll see build logs in real-time
- Once deployed, you'll get a URL like: `https://myclassroom-api.onrender.com`

---

## 🎉 **That's It! Your API is LIVE!**

### **Test Your API:**

```powershell
# Replace with your actual Render URL
$API_URL = "https://myclassroom-api.onrender.com"

# Test registration
curl -X POST "$API_URL/auth/register" `
  -H "Content-Type: application/json" `
  -d '{"username":"testuser","email":"test@example.com","password":"password123","role":"STUDENT"}'

# Test login
curl -X POST "$API_URL/auth/login" `
  -H "Content-Type: application/json" `
  -d '{"email":"test@example.com","password":"password123"}'
```

---

## 📝 **Important Notes:**

### **Free Tier Limits:**
- ✅ **Always FREE** (no trial expiration)
- ✅ PostgreSQL database included (90 days free, then switches to paid)
- ⚠️ **Auto-sleeps** after 15 minutes of inactivity
- ⚠️ First request after sleep takes ~30 seconds to wake up
- ✅ 750 hours/month of runtime

### **Keeping Your Service Active:**
If you want to prevent sleep, you can:
1. Use a free uptime monitor like [UptimeRobot](https://uptimerobot.com)
2. Ping your API every 10 minutes
3. Or upgrade to Render's paid plan ($7/month for always-on)

---

## 🔧 **Auto-Deploy:**

Every time you push to GitHub, Render automatically redeploys! 🚀

```powershell
git add .
git commit -m "Update API"
git push
# Render auto-deploys!
```

---

## 🆘 **Troubleshooting:**

### **Build Fails?**
- Check build logs in Render dashboard
- Make sure `gradlew` has execute permissions
- Verify Java version compatibility

### **Database Connection Error?**
- Double-check `DATABASE_URL` format
- Make sure database and web service are in same region
- Use **Internal Database URL** (not external)

### **App Crashes?**
- Check logs in Render dashboard
- Verify all environment variables are set
- Make sure PORT is set to `10000`

---

## 🎯 **Your Deployed URL:**

Once deployed, your API will be live at:
```
https://your-app-name.onrender.com
```

### **Available Endpoints:**
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `POST /classroom/create` - Create classroom (JWT required)
- `POST /classroom/join` - Join classroom (JWT required)
- And more...

---

## 💡 **Pro Tips:**

1. **Custom Domain**: You can add your own domain in Render settings
2. **Monitoring**: Check logs and metrics in Render dashboard
3. **Environment Variables**: Update anytime without redeploying
4. **SSL**: Automatic HTTPS included for free! 🔒

---

**Need help?** Render has great documentation: https://render.com/docs

**Enjoy your FREE deployed API!** 🎊
