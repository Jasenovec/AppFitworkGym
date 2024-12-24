package com.fitworkgym.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitworkgym.data.model.Equipo
import com.fitworkgym.data.model.Trainer
import com.fitworkgym.databinding.ContentEquipoBinding
import com.fitworkgym.databinding.ContentTrainerAdminBinding
import com.fitworkgym.databinding.ContentTrainerBinding


class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private val trainers = object : DiffUtil.ItemCallback<Trainer>() {
        override fun areItemsTheSame(oldItem: Trainer, newItem: Trainer): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Trainer, newItem: Trainer): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, trainers)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentTrainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    class ViewHolder(val binding: ContentTrainerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.binding.apply {
            trainer.text = data.name
            schedule.text = data.schedule
            Glide.with(profileTrainer.context).load(data.image)
                .into(profileTrainer)
        }
    }

}

/*class ActivitiesAdapter(private val activities: List<Activities>) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_activities, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameActivity: TextView = itemView.findViewById(R.id.type_activity)
        private val nameInstructor: TextView = itemView.findViewById(R.id.textNameInstructor)
        private val schedule: TextView = itemView.findViewById(R.id.textSchedule)
        private val image: ImageView = itemView.findViewById(R.id.activity_trainer)

        fun bind(activities: Activities) {
            nameActivity.text = activities.nameActivity
            nameInstructor.text = activities.nameInstructor
            schedule.text = activities.schedule
            image.setImageResource(activities.image)
        }
    }
}*/

class EquipoAdapter() : RecyclerView.Adapter<EquipoAdapter.ViewHolder>() {

    private val equipos = object : DiffUtil.ItemCallback<Equipo>() {
        override fun areItemsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, equipos)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentEquipoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    class ViewHolder(val binding: ContentEquipoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.binding.apply {
            nameEquipo.text = data.name
            descriptionEquipo.text = data.description
            Glide.with(imgEquipo.context).load(data.image)
                .into(imgEquipo)
        }
    }

}

// Adapter Trainer para Admin

class AdapterAdmin() : RecyclerView.Adapter<AdapterAdmin.ViewHolder>() {

    private val trainers = object : DiffUtil.ItemCallback<Trainer>() {
        override fun areItemsTheSame(oldItem: Trainer, newItem: Trainer): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Trainer, newItem: Trainer): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, trainers)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentTrainerAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    class ViewHolder(val binding: ContentTrainerAdminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        holder.binding.apply {
            trainerAdmin.text = data.name
            scheduleAdmin.text = data.schedule
            phoneAdmin.text = data.phone.toString()
            Glide.with(profileTrainerAdmin.context).load(data.image)
                .into(profileTrainerAdmin)
        }
    }

}