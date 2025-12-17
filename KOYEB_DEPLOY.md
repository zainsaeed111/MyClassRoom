# 🚀 Deploy Free Backend (Koyeb + Neon)

Since you found Render/Fly "not enough" (likely due to sleep/limits), here is the **Best Free Stack** for 2024/2025.

**The Stack:**
- **Compute (App Host):** [Koyeb](https://www.koyeb.com/)
  - *Why?* Faster, generous free tier (Instance doesn't sleep in some regions, or wakes up very fast).
- **Database:** [Neon.tech](https://neon.tech/)
  - *Why?* Render's free DB expires in 90 days. Neon is **Free Forever** (0.5GB storage).

---

## Part 1: get a Free Database (Neon.tech)

1. Go to [https://neon.tech](https://neon.tech) and Sign Up.
2. Create a new Project.
3. It will give you a **Connection String** immediately (looks like `postgres://user:pass@ep-xyz.aws.neon.tech/neondb...`).
4. **Copy this string.** You will need it later.

---

## Part 2: Deploy App on Koyeb

1. **Push your code to GitHub** (if you haven't already).
   ```bash
   git add .
   git commit -m "Added Dockerfile and DB fix"
   git push origin main
   ```

2. Go to [https://www.koyeb.com](https://www.koyeb.com) and Sign Up.
3. Click **Create App** (or "Deploy").
4. Select **GitHub** as the source.
5. Select your repository (`MyClassRoom`).
6. **Builder**: Choose **Docekrfile**.
   - Koyeb should automatically detect these, but if asked, select "Dockerfile".

7. **Environment Variables** (Expand the "Advanced" or "Environment" section):
   Add the following variables:
   
   | Key | Value |
   |-----|-------|
   | `DATABASE_URL` | Paste your **Neon Connection String** here |
   | `PORT` | `8080` |
   | `JWT_SECRET` | `somelongsecretkey` |
   | `JWT_DOMAIN` | `https://your-koyeb-app-name.koyeb.app/` |
   | `JWT_AUDIENCE` | `jwt-audience` |
   | `JWT_REALM` | `ktor sample app` |

8. **Instance Type**: Select **Eco (Free)** or **Nano**.
   - Make sure to pick a region that says "Free Tier available" (usually Washington D.C or Frankfurt).

9. Click **Deploy**.

---

## Why this is better than Render?
1. **Database never expires** (Neon logic).
2. **Performance**: Koyeb runs on microVMs which are very fast.
3. **No Sleep**: If you get the "Eco" instance active, it often stays responsive.

---

## Alternative: Local Testing (Easiest for Mobile Dev)

If you only need this for **testing your mobile app** while developing, you don't even need to deploy!

1. Run your server in IntelliJ/Terminal (`./gradlew run`).
2. Download **Ngrok** (https://ngrok.com/download).
3. Run command: `ngrok http 8080`
4. Copy the `https://xxxx.ngrok-free.app` URL.
5. Use that URL in your Mobile App.

*Pros:* fastest, debug locally, checks logs instantly.
*Cons:* laptop must be on.
